package org.aery.study.java.executors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NewSingleThreadScheduledExecutorStudy {

    @Test
    public void schedule() throws InterruptedException {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

        long delayMs = 1000;
        CountDownLatch latch = new CountDownLatch(1);
        AtomicLong checkPoint1 = new AtomicLong();
        AtomicLong checkPoint2 = new AtomicLong();

        checkPoint1.set(System.currentTimeMillis());
        ses.schedule(() -> { // 只會執行一次
            checkPoint2.set(System.currentTimeMillis());
            System.out.println(checkPoint2.get());
            latch.countDown();
        }, delayMs, TimeUnit.MILLISECONDS);

        latch.await();

        Assertions.assertThat(checkPoint2.get() - checkPoint1.get())
                .isGreaterThanOrEqualTo(delayMs);
    }

    /**
     * 我發現它會自動修正誤差, 當每次執行時間點與第一次執行時間點預期每次的執行時間差距越來越大時, 會自動修正讓誤差變小.
     */
    @Test
    public void scheduleAtFixedRate_process_in_period() throws InterruptedException {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

        long period = 100;
        long initialDelay = period;
        int times = 20;
        AtomicInteger count = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);
        List<Long> checkPoint = new ArrayList<>();

        long startTs = System.currentTimeMillis();
        checkPoint.add(startTs);
        ses.scheduleAtFixedRate(() -> {
            long ts = System.currentTimeMillis();
            System.out.printf("%04d, %04d, %02d, %d%n",
                    (count.get() + 1) * period, // 預期每次執行時間點
                    ts - startTs, // 實際每次執行時間點
                    (ts - startTs) % period, // 預期與實際誤差
                    ts - checkPoint.get(count.get()) // 與上一次執行的時間差
            );
            checkPoint.add(ts);

            int bout = count.incrementAndGet();
            if (bout == times) {
                ses.shutdown();
                latch.countDown();
            }
        }, initialDelay, period, TimeUnit.MILLISECONDS);

        latch.await();

        long eachEarlyBuffer = period - 20; // 預期執行時間點被schedule修正而提早的buffer(20ms)
        long eachDelayedBuffer = period + 30; // 預期command執行為30ms內要完成

        Assertions.assertThat(checkPoint).hasSize(times + 1);

        Iterator<Long> iterator = checkPoint.iterator();
        long previous = iterator.next();
        while (iterator.hasNext()) {
            Long current = iterator.next();
            Assertions.assertThat(current - previous)
                    .isGreaterThanOrEqualTo(eachEarlyBuffer)
                    .isLessThan(eachDelayedBuffer);
            previous = current;
        }
    }

}
