package org.aery.study.spring.redis.service.impl;

import com.sun.istack.internal.logging.Logger;
import org.aery.study.spring.redis.service.api.RedisDataExplorer;
import org.aery.study.spring.redis.service.vo.RedisExploredResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RedisDataExplorerPreset implements RedisDataExplorer {

    private final Logger logger = Logger.getLogger(getClass());

    private final Map<DataType, Function<String, Object>> redisDataFetcherMap = new HashMap<>();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
            Object data = fetchAllData ? this.redisDataFetcherMap.get(dataType).apply(key) : null;
            result.put(key, dataType, data);
        });

        return result;
    }

    @Override
    public void printExplored(String pattern, boolean fetchAllData) {
        RedisExploredResult result = explore(pattern, fetchAllData);

        StringBuilder sb = new StringBuilder();
        Consumer<String> line = (msg) -> sb.append(msg).append(System.lineSeparator());

        line.accept("redis explore key pattern \"" + pattern + "\" : {");
        result.foreach((key, tuple) -> {
            line.accept("\t" + tuple.toString(key));
        });
        line.accept("}");

        this.logger.info(sb.toString());
    }

    public String fetchString(String key) {
        return this.redisTemplate.execute((RedisCallback<String>) (connection) -> {
//            RedisStringCommands stringCommands = connection.stringCommands();
//            byte[] string = stringCommands.get(key.getBytes());
            byte[] string = connection.get(key.getBytes());
            return new String(string);
        });
    }

    public List<String> fetchList(String key) {
        return this.redisTemplate.execute((RedisCallback<List<String>>) (connection) -> {
//            RedisListCommands listCommands = connection.listCommands();
//            List<byte[]> list = listCommands.lRange(key.getBytes(), 0, -1);
            List<byte[]> list = connection.lRange(key.getBytes(), 0, -1);
            return list.stream().map(String::new).collect(Collectors.toList());
        });
    }

    public Set<String> fetchSet(String key) {
        return this.redisTemplate.execute((RedisCallback<Set<String>>) (connection) -> {
//            RedisSetCommands setCommands = connection.setCommands();
//            Set<byte[]> set = setCommands.sMembers(key.getBytes());
            Set<byte[]> set = connection.sMembers(key.getBytes());
            return set.stream().map(String::new).collect(Collectors.toSet());
        });
    }

    public LinkedHashSet<String> fetchZset(String key) {
        return this.redisTemplate.execute((RedisCallback<LinkedHashSet<String>>) (connection) -> {
//            RedisZSetCommands zsetCommands = connection.zSetCommands();
//            Set<byte[]> set = zsetCommands.zRangeByLex(key.getBytes());
            Set<byte[]> set = connection.zRangeByLex(key.getBytes());

            LinkedHashSet<String> result = new LinkedHashSet<>();
            set.stream().map(String::new).forEachOrdered(result::add);
            return result;
        });
    }

    public Map<String, String> fetchHash(String key) {
        return this.redisTemplate.execute((RedisCallback<Map<String, String>>) (connection) -> {
//            RedisHashCommands hashCommands = connection.hashCommands();
//            Map<byte[], byte[]> map = hashCommands.hGetAll(key.getBytes());
            Map<byte[], byte[]> map = connection.hGetAll(key.getBytes());

            return map.entrySet().stream().map((entry) -> {
                String hk = new String(entry.getKey());
                String hv = new String(entry.getValue());
                return Collections.singletonMap(hk, hv);
            }).reduce(new HashMap<>(), (map1, map2) -> {
                map1.putAll(map2);
                return map1;
            });
        });
    }

}
