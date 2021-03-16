package org.aery.study.spring.redis._test;

import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@TestConfiguration
public class RedisEmbeddedServerConfig {

    private RedisServer redisServer;

    public RedisEmbeddedServerConfig() {
        this.redisServer = new RedisServer();
    }

    @PostConstruct
    public void postConstruct() {
        this.redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        this.redisServer.stop();
    }

}
