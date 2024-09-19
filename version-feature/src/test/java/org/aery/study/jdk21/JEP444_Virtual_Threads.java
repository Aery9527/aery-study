package org.aery.study.jdk21;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.LongConsumer;
import java.util.function.LongSupplier;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

/**
 * 關於 VT 預設所使用的 PT 的 thread-pool 請看 {@link VirtualThread#createDefaultScheduler()}
 */
public class JEP444_Virtual_Threads {

    private static final Logger LOGGER = LoggerFactory.getLogger(JEP444_Virtual_Threads.class);

    /**
     * 用來展示 VT 與 PT 的數量的差異
     */
    public static void main(String[] args) throws InterruptedException {
        RandomGenerator randomGenerator = RandomGenerator.getDefault();
        int vtNumber = 100;

        LongSupplier restRandom = () -> randomGenerator.nextLong(1, 3);

        LongConsumer rest = sleep -> {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable task = () -> rest.accept(restRandom.getAsLong()); // 這邊非常單純只有模擬邏輯執行時間, 可以觀察到 PT 並不會使用太多
//        Runnable task = () -> { // 這邊有額外操作log, 會發現啟用的 PT 變多了
//            long sleep = restRandom.getAsLong();
//            LOGGER.info("start with sleep {}", sleep);
//            rest.accept(sleep);
//            LOGGER.info("end   with sleep {}", sleep);
//        };

        Set<Thread> allThreadBeforeUseVT = Thread.getAllStackTraces().keySet();

        long start = System.currentTimeMillis();
        List<Thread> vts = IntStream.rangeClosed(1, vtNumber)
                .mapToObj(i -> Thread.ofVirtual() // 使用 VT
                        .name(String.format("vt-%04d", i)) // 沒有給name就會是空白
                        .start(task))
                .toList();
        for (Thread vt : vts) {
            vt.join();
        }
        long end = System.currentTimeMillis();

        Set<Thread> allThreadAfterUseVT = Thread.getAllStackTraces().keySet();

        List<Thread> ptList = allThreadAfterUseVT.stream().filter(t -> !allThreadBeforeUseVT.contains(t)).toList();

        ptList.forEach(pt -> LOGGER.info("平台執行緒: {}", pt.getName()));
        LOGGER.info("虛擬執行緒(VT)建立 {} 個, 平台執行緒(PT)使用 {} 個, 共耗時 {} ms", vtNumber, ptList.size(), end - start);
    }

    /**
     * 最簡單的使用方式, 但不建議在正式環境這樣使用, 因為沒有 thread name 會很難追蹤
     */
    @Test
    void startVirtualThread() throws InterruptedException {
        LOGGER.info("start");
        Thread.startVirtualThread(() -> LOGGER.info("HI")).join();
        LOGGER.info("end");
    }

    @Test
    void OfVirtual_start() throws InterruptedException {
        Thread.Builder.OfVirtual vtBuilder = Thread.ofVirtual();

        Thread vt1 = vtBuilder.start(() -> LOGGER.info("HI")); // 會直接執行
        Thread vt2 = vtBuilder.start(() -> LOGGER.info("HI")); // 會直接執行

        vt1.join();
        vt2.join();
    }

    @Test
    void OfVirtual_unstarted() throws InterruptedException {
        Thread.Builder.OfVirtual vtBuilder = Thread.ofVirtual();

        Thread vt1 = vtBuilder.unstarted(() -> LOGGER.info("HI"));
        Thread vt2 = vtBuilder.unstarted(() -> LOGGER.info("HI"));

        // 稍後手動執行
        LOGGER.info("start");
        vt1.start();
        vt2.start();
        vt1.join();
        vt2.join();
        LOGGER.info("end");
    }

    @Test
    void OfVirtual_name() throws InterruptedException {
        Thread.Builder.OfVirtual vtBuilder = Thread.ofVirtual();

        vtBuilder.name("vt-001"); // 固定 thead name
        vtBuilder.start(() -> LOGGER.info("HI")).join();
        vtBuilder.start(() -> LOGGER.info("HI")).join();

        vtBuilder.name("vt-", 2); // 遞增 thead name
        vtBuilder.start(() -> LOGGER.info("HI")).join();
        vtBuilder.start(() -> LOGGER.info("HI")).join();
    }

    @Test
    void OfVirtual_uncaughtExceptionHandler() throws InterruptedException {
        Thread.Builder.OfVirtual vtBuilder = Thread.ofVirtual();

        Assertions.assertThatThrownBy(() -> vtBuilder.uncaughtExceptionHandler(null)).isInstanceOf(NullPointerException.class);

        Thread vt1 = vtBuilder.unstarted(() -> {
            throw new RuntimeException("1");
        });

        vtBuilder.uncaughtExceptionHandler((t, e) -> LOGGER.error("{}", t, e));

        Thread vt2 = vtBuilder.unstarted(() -> {
            throw new RuntimeException("2");
        });

        LOGGER.info("start");
        vt1.start();
        vt2.start();
        vt1.join();
        vt2.join();
        LOGGER.info("end");
    }

    @Test
    void OfVirtual_inheritInheritableThreadLocals() throws InterruptedException {
        InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
        inheritableThreadLocal.set("kerker"); // 設定父執行緒的值

        Runnable r = () -> LOGGER.info("{}", inheritableThreadLocal.get());

        Thread.Builder.OfVirtual vtBuilder = Thread.ofVirtual().name("vt", 1);

        Thread vt1 = vtBuilder.unstarted(r);

        vtBuilder.inheritInheritableThreadLocals(false); // 預設是 true

        Thread vt2 = vtBuilder.unstarted(r);

        vtBuilder.inheritInheritableThreadLocals(true);

        Thread vt3 = vtBuilder.unstarted(r);

        LOGGER.info("start");
        vt1.start();
        vt2.start();
        vt3.start();
        vt1.join();
        vt2.join();
        vt3.join();
        LOGGER.info("end");
    }

    /**
     * 可以產生 {@link ThreadFactory} 結合 {@link Executors} 使用
     */
    @Test
    void OfVirtual_factory() {
        ThreadFactory threadFactory = Thread.ofVirtual().name("vt-", 0).factory();
        try (ExecutorService es = Executors.newCachedThreadPool(threadFactory);) {
            IntStream.range(0, 10).forEach(i -> es.submit(() -> LOGGER.info("{}", i)));
        }
    }

    /**
     * 直接使用 {@link Executors#newVirtualThreadPerTaskExecutor()} 來建立 {@link ExecutorService},
     * 不過一樣不建議在正式環境這樣使用, 因為沒有 thread name 會很難追蹤
     */
    @Test
    void Executors_newVirtualThreadPerTaskExecutor() {
        try (ExecutorService es = Executors.newVirtualThreadPerTaskExecutor();) {
            IntStream.range(0, 10).forEach(i -> es.submit(() -> LOGGER.info("{}", i)));
        }
    }

}
