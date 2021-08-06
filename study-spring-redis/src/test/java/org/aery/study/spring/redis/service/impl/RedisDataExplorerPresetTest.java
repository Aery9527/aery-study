package org.aery.study.spring.redis.service.impl;

import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.aery.study.spring.redis.service.vo.RedisExploredResult;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisEmbeddedServerConfig.class)
@ActiveProfiles("test")
public class RedisDataExplorerPresetTest {

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

    @Autowired
    private RedisDataExplorerPreset redisDataExplorerPreset;

    @Test
    public void explore() {
        List<String> testDatas = Stream.of("Aery", "Rion", "Fuko", "Aery", "Alicia").collect(Collectors.toList());

        String keyOfValue = "keyOfValue";
        String keyOfList = "keyOfList";
        String keyOfSet = "keyOfSet";
        String keyOfZset = "keyOfZset";
        String keyOfHash = "keyOfHash";

        String dataOfValue = "kerker";
        List<String> dataOfList = new ArrayList<>(testDatas); // 有序可重複集合
        Set<String> dataOfSet = new HashSet<>(testDatas); // 無序不可重複集合
        Set<String> dataOfZset = new LinkedHashSet<>(testDatas); // 有序不可重複集合
        Map<String, String> dataOfHash = new HashMap<>();

        redisValueOps.set(keyOfValue, dataOfValue);
        for (int i = 0; i < testDatas.size(); i++) {
            String data = testDatas.get(i);
            redisListOps.rightPush(keyOfList, data);
            redisSetOps.add(keyOfSet, data);
            redisZsetOps.add(keyOfZset, data, i);
            redisHashOps.put(keyOfHash, String.valueOf(i), data);
            dataOfHash.put(String.valueOf(i), data);
        }

        RedisExploredResult result = this.redisDataExplorerPreset.explore();
        Map<String, RedisExploredResult.Tuple> datas = result.getDatas();

        Assertions.assertThat(datas).containsOnlyKeys(keyOfValue, keyOfList, keyOfSet, keyOfZset, keyOfHash);

        String valueFromRedis = datas.get(keyOfValue).getDataToString();
        List<String> listFromRedis = datas.get(keyOfList).getDataToList();
        Set<String> setFromRedis = datas.get(keyOfSet).getDataToSet();
        Set<String> zsetFromRedis = datas.get(keyOfZset).getDataToZset();
        Map<String, String> tupleOfHash = datas.get(keyOfHash).getDataToHash();

        Assertions.assertThat(valueFromRedis).isEqualTo(dataOfValue);
        Assertions.assertThat(listFromRedis).containsExactlyElementsOf(dataOfList);
        Assertions.assertThat(setFromRedis).containsExactlyInAnyOrderElementsOf(dataOfSet);
        Assertions.assertThat(zsetFromRedis).containsExactlyElementsOf(dataOfZset);
        Assertions.assertThat(tupleOfHash).containsExactlyEntriesOf(dataOfHash);
    }

}
