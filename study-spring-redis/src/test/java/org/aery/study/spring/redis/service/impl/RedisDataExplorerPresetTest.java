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
import java.util.function.UnaryOperator;
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
        List<String> dataOfList = new ArrayList<>();
        Set<String> dataOfSet = new LinkedHashSet<>();
        Set<String> dataOfZset = new LinkedHashSet<>();
        Map<String, String> dataOfHash = new HashMap<>();

        UnaryOperator<String> toJsonFormat = (s) -> "\"" + s + "\"";

        redisValueOps.set(keyOfValue, dataOfValue);
        for (int i = 0; i < testDatas.size(); i++) {
            String data = testDatas.get(i); // 因為透過jackson序列化, 會轉換成json格式

            redisListOps.rightPush(keyOfList, data);
            dataOfList.add(toJsonFormat.apply(data));

            redisSetOps.add(keyOfSet, data);
            dataOfSet.add(toJsonFormat.apply(data));

            redisZsetOps.add(keyOfZset, data, i); // 因為LinkedHashSet的行為是優先加入的維持在前
            dataOfZset.remove(toJsonFormat.apply(data)); // 但redis的zset的行為是後加入的會替換掉前面的位置
            dataOfZset.add(toJsonFormat.apply(data)); // 所以這裡會先移除本來就有的data再加入, 以模擬redis zset最終的排序

            redisHashOps.put(keyOfHash, String.valueOf(i), data);
            dataOfHash.put(String.valueOf(i), toJsonFormat.apply(data));
        }

        RedisExploredResult result = this.redisDataExplorerPreset.explore();
        Map<String, RedisExploredResult.Tuple> datas = result.getDatas();

        Assertions.assertThat(datas).containsOnlyKeys(keyOfValue, keyOfList, keyOfSet, keyOfZset, keyOfHash);

        String valueFromRedis = datas.get(keyOfValue).getDataToString();
        List<String> listFromRedis = datas.get(keyOfList).getDataToList();
        Set<String> setFromRedis = datas.get(keyOfSet).getDataToSet();
        Set<String> zsetFromRedis = datas.get(keyOfZset).getDataToZset();
        Map<String, String> tupleOfHash = datas.get(keyOfHash).getDataToHash();

        Assertions.assertThat(valueFromRedis).isEqualTo(toJsonFormat.apply(dataOfValue));
        Assertions.assertThat(listFromRedis).containsExactlyElementsOf(dataOfList);
        Assertions.assertThat(setFromRedis).containsExactlyInAnyOrderElementsOf(dataOfSet);
        Assertions.assertThat(zsetFromRedis).containsExactlyElementsOf(dataOfZset);
        Assertions.assertThat(tupleOfHash).containsExactlyEntriesOf(dataOfHash);
    }

}
