package org.aery.study.spring.redis;

import io.lettuce.core.RedisCommandExecutionException;
import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = RedisEmbeddedServerConfig.class)
@ActiveProfiles("test")
@DirtiesContext
public class ListOperationsTest {

    @Autowired
    private ListOperations<String, Object> redisListOps;

    @Test
    public void set() {
        String key = Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);

        // 對一個空集合set會出錯
        Assertions.assertThatThrownBy(() -> this.redisListOps.set(key, 0, "123"))
                .isInstanceOf(RedisSystemException.class)
                .hasCauseInstanceOf(RedisCommandExecutionException.class)
                .hasMessageContaining("ERR no such key");

        this.redisListOps.leftPush(key, "1"); // index 1
        this.redisListOps.leftPush(key, "2"); // index 0

        // 對一個錯的index set會出錯
        Assertions.assertThatThrownBy(() -> this.redisListOps.set(key, 3, "123"))
                .isInstanceOf(RedisSystemException.class)
                .hasCauseInstanceOf(RedisCommandExecutionException.class)
                .hasMessageContaining("ERR index out of range");

        List<String> all = getAll(key);
        Assertions.assertThat(all).containsExactly("2", "1");
    }

    @Test
    public void rightPushIfPresent() {
        String key = "kerker";
        Assertions.assertThat(this.redisListOps.rightPushIfPresent(key, "1")).isEqualTo(0);
        Assertions.assertThat(this.redisListOps.rightPush(key, "2")).isEqualTo(1);
        Assertions.assertThat(this.redisListOps.rightPushIfPresent(key, "3")).isEqualTo(2);

        List<String> all = getAll(key);
        Assertions.assertThat(all).containsExactly("2", "3");
    }

    @Test
    public void range() {
        String key = Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);
        String val = "Rion";

        Object a = this.redisListOps.index(key, 0);
        Assertions.assertThat(a).isNull();

        this.redisListOps.rightPush(key, val);
        Object b = this.redisListOps.index(key, 0);
        Assertions.assertThat(b).isEqualTo(val);
    }

    @Test
    public void remove() {
        String key = Long.toString(System.currentTimeMillis(), Character.MAX_RADIX);

        this.redisListOps.rightPush(key, "A");
        this.redisListOps.rightPush(key, "B");
        this.redisListOps.rightPush(key, "C");
        this.redisListOps.rightPush(key, "D");
        this.redisListOps.rightPush(key, "A");
        this.redisListOps.rightPush(key, "B");
        this.redisListOps.rightPush(key, "C");
        this.redisListOps.rightPush(key, "D");
        this.redisListOps.rightPush(key, "A");
        this.redisListOps.rightPush(key, "B");
        this.redisListOps.rightPush(key, "C");
        this.redisListOps.rightPush(key, "D");

        List<String> all = getAll(key);
        Assertions.assertThat(all).containsExactly(
                "A", "B", "C", "D",
                "A", "B", "C", "D",
                "A", "B", "C", "D"
        );

        this.redisListOps.remove(key, 0, "A"); // count=0, 刪除所有相同內容
        all = getAll(key);
        Assertions.assertThat(all).containsExactly(
                "B", "C", "D",
                "B", "C", "D",
                "B", "C", "D"
        );

        this.redisListOps.remove(key, 2, "B"); // count>0, 從左邊開始刪除相同內容, 刪除count個
        all = getAll(key);
        Assertions.assertThat(all).containsExactly(
                /*"B",*/ "C", "D",
                /*"B",*/ "C", "D",
                "B", "C", "D"
        );

        this.redisListOps.remove(key, -1, "C"); // count<0, 從右邊開始刪除相同內容, 刪除|count|個
        all = getAll(key);
        Assertions.assertThat(all).containsExactly(
                "C", "D",
                "C", "D",
                "B", /*"C",*/ "D"
        );
    }

    public List<String> getAll(String key) {
        return this.redisListOps.range(key, 0, -1).stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }
}

