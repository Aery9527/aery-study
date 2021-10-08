package org.aery.study.spring.redis;

import io.lettuce.core.RedisCommandExecutionException;
import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisEmbeddedServerConfig.class)
@ActiveProfiles("test")
public class ListOperationsTest {

    @Autowired
    private ListOperations<String, Object> redisListOps;

    @Test
    public void set() {
        String key = "kerker";

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

        List<String> result = this.redisListOps.range(key, 0, -1).stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        Assertions.assertThat(result).containsExactly("2", "1");
    }

    @Test
    public void rightPushIfPresent() {
        System.out.println(this.redisListOps.rightPushIfPresent("kerker", "1"));
        System.out.println(this.redisListOps.rightPush("kerker", "2"));
        System.out.println(this.redisListOps.rightPushIfPresent("kerker", "3"));
    }

}

