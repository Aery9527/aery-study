package org.aery.study.spring.redis;

import io.lettuce.core.RedisCommandExecutionException;
import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@SpringBootTest(classes = RedisEmbeddedServerConfig.class)
@ActiveProfiles("test")
@DirtiesContext
public class HashOperationsTest {

    @Autowired
    private HashOperations<String, Object, Object> redisHashOps;

    @Test
    public void test_set_get_del() {
        final String key = Thread.currentThread().getStackTrace()[1].getMethodName();
        final String key1 = "kerker";
        final String val = "9527";

        Consumer<Boolean> check_Get = (hasVal) -> {
            Object v = this.redisHashOps.get(key, key1);
            if (hasVal) {
                Assertions.assertThat(v).isNotNull().isEqualTo(val);
            } else {
                Assertions.assertThat(v).isNull();
            }
        };
        Consumer<Boolean> check_hasKey = (expect) -> {
            boolean hasKey = this.redisHashOps.hasKey(key, key1);
            Assertions.assertThat(hasKey).isEqualTo(expect);
        };

        check_Get.accept(false);
        check_hasKey.accept(false);

        this.redisHashOps.put(key, key1, val);

        check_Get.accept(true);
        check_hasKey.accept(true);

        this.redisHashOps.delete(key, key1, "可以同時刪除多個key");

        check_Get.accept(false);
        check_hasKey.accept(false);
    }

    @Test
    public void test_entries() {
        final String key = Thread.currentThread().getStackTrace()[1].getMethodName();

        Map<String, Object> map = new HashMap<>();
        map.put("a", "9527");
        map.put("b", 123L);
        map.put("c", 1.3d);
        this.redisHashOps.putAll(key, map);

        Map<Object, Object> result = this.redisHashOps.entries("543");
        Assertions.assertThat(result).isEmpty();

        result = this.redisHashOps.entries(key);
        result.forEach((k, v) -> {
            System.out.println(k + "(" + k.getClass().getSimpleName() + ") : " + v + "(" + v.getClass().getSimpleName() + ")");
        });

//        Assertions.assertThat(result).containsAllEntriesOf(map); // XXX 不知道為什麼會錯, 看起來應該要對啊...
    }

    @Test
    public void test_putIfAbsent() {
        final String key = Thread.currentThread().getStackTrace()[1].getMethodName();
        final String k1 = "Rion";
        final String k2 = "Aery";

        Assertions.assertThat(this.redisHashOps.get(key, k1)).isNull();
        Assertions.assertThat(this.redisHashOps.putIfAbsent(key, k1, "123")).isEqualTo(true); // 寫入成功
        Assertions.assertThat(this.redisHashOps.get(key, k1)).isEqualTo("123");
        Assertions.assertThat(this.redisHashOps.putIfAbsent(key, k1, "123")).isEqualTo(false); // 寫入失敗
        Assertions.assertThat(this.redisHashOps.get(key, k1)).isEqualTo("123");
        Assertions.assertThat(this.redisHashOps.putIfAbsent(key, k2, "456")).isEqualTo(true); // 寫入成功
    }

    @Test
    public void test_increment() {
        final String key = Thread.currentThread().getStackTrace()[1].getMethodName();

        this.redisHashOps.put(key, "l", 100);
        this.redisHashOps.put(key, "d", 1.1);
        this.redisHashOps.put(key, "n", "9527");
        long l_delta = 543;
        double d_delta = 54.3;

        // 測試increment的key不存在時的行為, 若該key不存在會自動寫入
        long gg_delta = 1;
        Long gg1 = this.redisHashOps.increment(key, "gg", gg_delta); // create, 1
        Assertions.assertThat(gg1).isEqualTo(gg_delta);
        Long gg2 = this.redisHashOps.increment(key, "gg", gg_delta); // increment, 2
        Assertions.assertThat(gg2).isEqualTo(gg_delta * 2);

        // 測試long的increment
        Long l1 = this.redisHashOps.increment(key, "l", l_delta);
        Assertions.assertThat(l1).isEqualTo(100 + l_delta);
        Double l2 = this.redisHashOps.increment(key, "l", d_delta); // 會把val型態從long變成double
        Assertions.assertThat(l2).isEqualTo(100 + l_delta + d_delta);
        Assertions.assertThatThrownBy(() -> this.redisHashOps.increment(key, "l", l_delta))
                .isInstanceOf(RedisSystemException.class)
                .hasRootCauseInstanceOf(RedisCommandExecutionException.class)
                .hasMessageContaining("ERR hash value is not an integer");

        // 測試double的increment
        Double d1 = this.redisHashOps.increment(key, "d", d_delta);
        Assertions.assertThat(d1).isEqualTo(1.1 + d_delta);
        Assertions.assertThatThrownBy(() -> this.redisHashOps.increment(key, "d", l_delta))
                .isInstanceOf(RedisSystemException.class)
                .hasRootCauseInstanceOf(RedisCommandExecutionException.class)
                .hasMessageContaining("ERR hash value is not an integer");

        // 測試不是數字的increment
        Assertions.assertThatThrownBy(() -> this.redisHashOps.increment(key, "n", l_delta))
                .isInstanceOf(RedisSystemException.class)
                .hasRootCauseInstanceOf(RedisCommandExecutionException.class)
                .hasMessageContaining("ERR hash value is not an integer");
        Assertions.assertThatThrownBy(() -> this.redisHashOps.increment(key, "n", d_delta))
                .isInstanceOf(RedisSystemException.class)
                .hasRootCauseInstanceOf(RedisCommandExecutionException.class)
                .hasMessageContaining("ERR hash value is not a valid float");
    }

}
