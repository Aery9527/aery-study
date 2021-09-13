package org.aery.study.spring.redis;

import io.lettuce.core.RedisCommandExecutionException;
import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.function.BooleanSupplier;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisEmbeddedServerConfig.class)
@ActiveProfiles("test")
public class ValueOperationsTest {

    public static class ValueObject {
        private String a;

        private String b;

        public ValueObject(String a, String b) {
            this.a = a;
            this.b = b;
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }
    }

    public enum Kerker {
        Aery, Rion;
    }

    @Autowired
    private ValueOperations<String, Object> redisValueOps;

    /**
     * 就一般的設定/取用/刪除
     */
    @Test
    public void test_set_get_del() {
        final String key = UUID.randomUUID().toString();
        final String val = "9527"; // 如果塞null會報IllegalArgumentException

        Runnable mustGetNull = () -> {
            Object get = this.redisValueOps.get(key);
            Assertions.assertThat(get).isEqualTo(null);
        };

        Runnable mustGetVal = () -> {
            this.redisValueOps.set(key, val);
            Object get = this.redisValueOps.get(key);
            Assertions.assertThat(get).isEqualTo(val);
        };

        BooleanSupplier delete = () -> {
            RedisOperations<String, Object> operations = this.redisValueOps.getOperations();
            return operations.delete(key);
        };

        try {
            mustGetNull.run();
            mustGetVal.run();
            Assertions.assertThat(delete.getAsBoolean()).isEqualTo(true); // 必須有從redis刪除資料
            Assertions.assertThat(delete.getAsBoolean()).isEqualTo(false); // 上一個刪除了key, 所以這個必須是失敗
            mustGetNull.run();
        } finally {
            delete.getAsBoolean();
        }
    }

    /**
     * 設定新值取出舊值, 使用Object
     */
    @Test
    public void test_getset_object() {
        final String key = UUID.randomUUID().toString();
        final ValueObject val1 = new ValueObject("1", "2"); // 因為使用ValueOperations, 所以會序列化成字串寫入
        final ValueObject val2 = new ValueObject("3", "4"); // 因為使用ValueOperations, 所以會序列化成字串寫入

        Object old1 = this.redisValueOps.getAndSet(key, val1);
        Assertions.assertThat(old1).isEqualTo(null);

        Object old2 = this.redisValueOps.getAndSet(key, val2);
        Assertions.assertThat(old2).isInstanceOf(Map.class);
        Map<String, Object> getedVal1 = (Map<String, Object>) old2;
        Assertions.assertThat(getedVal1.get("a")).isEqualTo("1");
        Assertions.assertThat(getedVal1.get("b")).isEqualTo("2");

        Map<String, Object> getedVal2 = (Map<String, Object>) this.redisValueOps.get(key);
        Assertions.assertThat(getedVal2.get("a")).isEqualTo("3");
        Assertions.assertThat(getedVal2.get("b")).isEqualTo("4");

        this.redisValueOps.set(key, Kerker.Rion);
        Object v = this.redisValueOps.get(key);
        Assertions.assertThat(Kerker.valueOf(v.toString())).isEqualTo(Kerker.Rion);
    }

    /**
     * 設定新值取出舊值, 使用Array
     */
    @Test
    public void test_getset_array() {
        final String key = UUID.randomUUID().toString();
        final List<ValueObject> val = new ArrayList<>();
        val.add(new ValueObject("1", "2"));
        val.add(new ValueObject("3", "4"));

        Object old1 = this.redisValueOps.getAndSet(key, val);
        Assertions.assertThat(old1).isEqualTo(null);

        Object getedVal = this.redisValueOps.get(key);
        Assertions.assertThat(getedVal).isInstanceOf(List.class);
        List<Object> valList = (List<Object>) getedVal;

        Object e1 = valList.get(0);
        Assertions.assertThat(e1).isInstanceOf(Map.class);
        Map<String, Object> m1 = (Map<String, Object>) e1;
        Assertions.assertThat(m1.get("a")).isEqualTo("1");
        Assertions.assertThat(m1.get("b")).isEqualTo("2");

        Object e2 = valList.get(1);
        Assertions.assertThat(e2).isInstanceOf(Map.class);
        Map<String, Object> m2 = (Map<String, Object>) e2;
        Assertions.assertThat(m2.get("a")).isEqualTo("3");
        Assertions.assertThat(m2.get("b")).isEqualTo("4");
    }

    /**
     * <a href="https://www.runoob.com/redis/strings-setrange.html">SETRANGE</a>
     * <a href="https://www.runoob.com/redis/strings-getrange.html">GETRANGE</a>
     * 可於redis端替換字串與擷取字串並回傳
     */
    @Test
    public void test_setrange_getrange() {
        // TODO
    }

    /**
     * <a href="https://www.runoob.com/redis/strings-setbit.html">SETBIT</a>
     * <a href="https://www.runoob.com/redis/strings-getbit.html">GETBIT</a>
     * 將value當作二進制操作bit
     */
    @Test
    public void test_setbig_getbig() {
        // TODO
    }

    /**
     * 一次設定多個key/取出多個val
     */
    @Test
    public void test_mset_mget() {
        Map<String, String> val = new HashMap();
        val.put("a", "1");
        val.put("b", "2");

        this.redisValueOps.multiSet(val);

        List<String> mgetKeys = new ArrayList<>();
        mgetKeys.add("a");
        mgetKeys.add("c");
        mgetKeys.add("b");

        List<Object> geted = this.redisValueOps.multiGet(mgetKeys);
        Assertions.assertThat(geted.get(0)).isEqualTo("1");
        Assertions.assertThat(geted.get(1)).isEqualTo(null);
        Assertions.assertThat(geted.get(2)).isEqualTo("2");
    }

    /**
     * 將value增量/減量
     */
    @Test
    public void test_incrby_decrby() {
        final String key = UUID.randomUUID().toString();
        final long val = 9527;

        Long geted = this.redisValueOps.increment(key); // 若無該key則會直接新增
        Assertions.assertThat(geted).isEqualTo(1L);

        this.redisValueOps.set(key, val);

        geted = this.redisValueOps.increment(key);
        Assertions.assertThat(geted).isEqualTo(val + 1);

        geted = this.redisValueOps.increment(key, 20);
        Assertions.assertThat(geted).isEqualTo(val + 21);

        geted = this.redisValueOps.decrement(key);
        Assertions.assertThat(geted).isEqualTo(val + 20);

        geted = this.redisValueOps.decrement(key, 20);
        Assertions.assertThat(geted).isEqualTo(val);

        this.redisValueOps.set(key, "9487");
        Assertions.assertThatThrownBy(() -> this.redisValueOps.decrement(key, 20))
                .isInstanceOf(RedisSystemException.class)
                .hasRootCauseInstanceOf(RedisCommandExecutionException.class)
                .hasMessageContaining("ERR value is not an integer or out of range")
        ;
    }

    /**
     * 直接在value屁股串上字串
     * FIXME 用jackson做轉換器好像在append上會有問題, 待解
     */
//    @Test
    public void test_append() {
        final String key = UUID.randomUUID().toString();
        final String val = "9527";

        Integer result = this.redisValueOps.append(key, val); // 若key不存在會新增
        Assertions.assertThat(result).isEqualTo(val.length());

        String random = UUID.randomUUID().toString().replaceAll("-", "");
        result = this.redisValueOps.append(key, random);
        Assertions.assertThat(result).isEqualTo(val.length() + random.length());

        Object geted = this.redisValueOps.get(key);
        Assertions.assertThat(geted).isEqualTo(val + random);
    }


}
