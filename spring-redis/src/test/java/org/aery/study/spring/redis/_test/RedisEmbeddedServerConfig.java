package org.aery.study.spring.redis._test;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

@TestConfiguration
public class RedisEmbeddedServerConfig implements InitializingBean, DisposableBean {

    private RedisServer redisServer;

    public RedisEmbeddedServerConfig() {
        this.redisServer = new RedisServer();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.redisServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.redisServer.stop();
        }));
    }

    @Override
    public void destroy() throws Exception {
        this.redisServer.stop();
    }

}
