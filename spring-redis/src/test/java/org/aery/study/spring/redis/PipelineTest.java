package org.aery.study.spring.redis;

import org.aery.study.spring.redis._test.RedisEmbeddedServerConfig;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisEmbeddedServerConfig.class)
@ActiveProfiles("test")
@DirtiesContext
public class PipelineTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void transaction() {
        Consumer<CountDownLatch> latchAwait = (latch) -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        String key = "kerker";
        String value = "9527";

        CountDownLatch latch1 = new CountDownLatch(1);
        CountDownLatch latch2 = new CountDownLatch(1);
        CountDownLatch latch3 = new CountDownLatch(1);
        CountDownLatch latch4 = new CountDownLatch(1);

        AtomicReference<String> checkPoint1 = new AtomicReference<>();
        AtomicReference<String> checkPoint2 = new AtomicReference<>();

        Thread watchThread = new Thread(() -> {
            ValueOperations<String, Object> valueOps = this.redisTemplate.opsForValue();

            latchAwait.accept(latch1);
            checkPoint1.set((String) valueOps.get(key));
            latch2.countDown();

            latchAwait.accept(latch3);
            checkPoint2.set((String) valueOps.get(key));
            latch4.countDown();
        });
        watchThread.start();

        this.redisTemplate.execute(new SessionCallback<List>() {
            @Override
            public List execute(RedisOperations operations) throws DataAccessException {
                operations.multi();

                ValueOperations valueOps = operations.opsForValue();
                valueOps.set(key, value);

                latch1.countDown();
                latchAwait.accept(latch2);

                return operations.exec();
            }
        });

        latch3.countDown();
        latchAwait.accept(latch4);

        Assertions.assertThat(checkPoint1.get()).isNull();
        Assertions.assertThat(checkPoint2.get()).isEqualTo(value);
    }

}
