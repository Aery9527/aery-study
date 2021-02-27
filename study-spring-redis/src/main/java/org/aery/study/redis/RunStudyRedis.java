package org.aery.study.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class RunStudyRedis {

    public static void main(String[] args) throws Throwable {
        try (ConfigurableApplicationContext application = SpringApplication.run(RunStudyRedis.class, args)) {
        }
    }

}
