package org.aery.study.spring.redis.service.impl;

import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.aery.study.spring.redis.service.api.RedisTransactionService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisEmbeddedServerConfig.class)
@ActiveProfiles("test")
public class RedisTransactionServicePresetTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisTransactionService redisTransactionService;

    @Test
    public void multiAndExec() {
        String key = "kerker";
        String initValue = "aaa";
        String updatedValue = "bbb";

        ValueOperations<String, Object> ops = this.redisTemplate.opsForValue();

        this.redisTransactionService.multiAndExec(key, initValue, updatedValue, false);
        Assertions.assertThat(ops.get(key)).isEqualTo(updatedValue);

        Assertions.assertThatThrownBy(() -> {
            this.redisTransactionService.multiAndExec(key, initValue, updatedValue, true);
        });
        Assertions.assertThat(ops.get(key)).isEqualTo(initValue);
    }

    @Test
    public void transaction() {
        String key = "kerker";
        String initValue = "aaa";
        String updatedValue = "bbb";

        ValueOperations<String, Object> ops = this.redisTemplate.opsForValue();

        ops.set(key, "1");
        this.redisTransactionService.transaction(key, initValue, updatedValue, false);
        Assertions.assertThat(ops.get(key)).isEqualTo(updatedValue);

        ops.set(key, "1");
        Assertions.assertThatThrownBy(() -> {
            this.redisTransactionService.transaction(key, initValue, updatedValue, true);
        });
        Assertions.assertThat(ops.get(key)).isEqualTo("1");
    }

}
