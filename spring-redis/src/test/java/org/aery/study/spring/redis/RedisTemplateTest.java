package org.aery.study.spring.redis;

import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisEmbeddedServerConfig.class)
@ActiveProfiles("test")
@DirtiesContext
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Test
    public void expire() throws InterruptedException {
        this.redisTemplate.opsForValue().set("kerker", "123");
        this.redisTemplate.expire("kerker", 10, TimeUnit.SECONDS);
    }

    @Test
    public void publish() {
        this.redisTemplate.convertAndSend("kerker", "wtf");
        this.redisMessageListenerContainer.addMessageListener((message, pattern) -> { // 可以動態加入
            System.out.println("QQ");
        }, new PatternTopic("gg"));
        this.redisTemplate.convertAndSend("gg", "wtf");
        System.out.println();
    }

}
