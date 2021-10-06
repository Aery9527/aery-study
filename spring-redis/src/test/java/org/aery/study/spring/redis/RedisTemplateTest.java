package org.aery.study.spring.redis;

import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisEmbeddedServerConfig.class)
@ActiveProfiles("test")
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void expire() {
        this.redisTemplate.opsForValue().set("kerker", "123");
        this.redisTemplate.expire("kerker", 10, TimeUnit.SECONDS);
    }

    @Test
    public void publish() {
        this.redisTemplate.convertAndSend("kerker", "wtf");
    }

}
