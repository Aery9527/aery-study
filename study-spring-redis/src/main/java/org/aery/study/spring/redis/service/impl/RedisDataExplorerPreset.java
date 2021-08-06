package org.aery.study.spring.redis.service.impl;

import org.aery.study.spring.redis.service.api.RedisDataExplorer;
import org.aery.study.spring.redis.service.vo.RedisExploredResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RedisDataExplorerPreset implements RedisDataExplorer {

    private final Map<DataType, Function<String, Object>> redisDataFetcherMap = new HashMap<>();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ValueOperations<String, Object> redisValueOps;

    @Autowired
    private ListOperations<String, Object> redisListOps;

    @Autowired
    private SetOperations<String, Object> redisSetOps;

    @Autowired
    private ZSetOperations<String, Object> redisZsetOps;

    @Autowired
    private HashOperations<String, Object, Object> redisHashOps;

    public RedisDataExplorerPreset() {
        this.redisDataFetcherMap.put(DataType.NONE, (key) -> null);
        this.redisDataFetcherMap.put(DataType.STRING, this::fetchString);
        this.redisDataFetcherMap.put(DataType.LIST, this::fetchList);
        this.redisDataFetcherMap.put(DataType.SET, this::fetchSet);
        this.redisDataFetcherMap.put(DataType.ZSET, this::fetchZset);
        this.redisDataFetcherMap.put(DataType.HASH, this::fetchHash);
    }

    @Override
    public RedisExploredResult explore(String pattern, boolean fetchAllData) {
        RedisExploredResult result = new RedisExploredResult();

        Set<String> keys = this.redisTemplate.keys(pattern);
        keys.forEach(key -> {
            DataType dataType = this.redisTemplate.type(key);
            Object data = this.redisDataFetcherMap.get(dataType).apply(key);
            result.put(key, dataType, data);
        });

        return result;
    }

    public String fetchString(String key) {
        return (String) this.redisValueOps.get(key);
    }

    public List<String> fetchList(String key) {
        List<Object> list = this.redisListOps.range(key, 0, -1);

        List<String> result = new ArrayList<>();
        list.stream().map((value) -> (String) value).forEachOrdered(result::add);
        return result;
    }

    public Set<String> fetchSet(String key) {
        Set<Object> set = this.redisSetOps.members(key);
        return set.stream().map(Object::toString).collect(Collectors.toSet());
    }

    public LinkedHashSet<String> fetchZset(String key) {
        Set<Object> set = this.redisZsetOps.range(key, 0, -1);

        LinkedHashSet<String> result = new LinkedHashSet<>();
        set.stream().map(Object::toString).forEachOrdered(result::add);
        return result;
    }

    public Map<String, String> fetchHash(String key) {
        Map<Object, Object> map = this.redisHashOps.entries(key);
        return map.entrySet().stream().map((entry) -> {
            String hk = (String) entry.getKey();
            String hv = (String) entry.getValue();
            return Collections.singletonMap(hk, hv);
        }).reduce(new HashMap<>(), (map1, map2) -> {
            map1.putAll(map2);
            return map1;
        });
    }

}
