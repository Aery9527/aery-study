package org.aery.study.spring.redis;


import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.aery.study.spring.redis.service.api.RedisDataExplorer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisEmbeddedServerConfig.class, properties = {
        "spring.redis.host=127.0.0.1",
        "spring.redis.port=6379"
})
@ActiveProfiles("test")
@DirtiesContext
public class HousekeepingOfTTL {

    @Autowired
    private RedisDataExplorer redisDataExplorer;

    @Test
    public void go() {
        this.redisDataExplorer.printExplored(true);
    }

}
