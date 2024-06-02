package org.aery.study.jdk9;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.concurrent.*;
import java.util.function.Consumer;

public class JEP213_Flow_SubmissionPublisher {

    class QuizSubscriber implements Flow.Subscriber<String> {

        private final Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

        private final String name;

        private final Consumer<String> delayAction;

        private Flow.Subscription subscription;

        private CountDownLatch wakeupManThread;

        QuizSubscriber(String name) {
            this(name, 0);
        }

        QuizSubscriber(String name, long delay) {
            this.name = name;
            this.delayAction = delay <= 0 ? (item) -> {
                // nothing
            } : (item) -> {
                try {
                    Thread.sleep(delay);
                    this.logger.info("{} finish  : {}", this.name, item); // 處理完資料
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            this.subscription.request(1); // 請求一個資料
        }

        @Override
        public void onNext(String item) {
            this.logger.info("{} receive : {}", this.name, item); // 處理完資料
            this.delayAction.accept(item); // 模擬邏輯處理耗時
            this.subscription.request(1); // 再請求一個資料
        }

        @Override
        public void onError(Throwable throwable) {
            this.logger.error("{}", this.name, throwable);
        }

        @Override
        public void onComplete() {
            this.logger.info("{}", this.name);
            this.wakeupManThread.countDown();
        }

        public void setWakeupManThread(CountDownLatch wakeupManThread) {
            this.wakeupManThread = wakeupManThread;
        }

    }

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    @Test
    void test() throws InterruptedException {
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        publisher.subscribe(new QuizSubscriber("Aery"));
        publisher.subscribe(new QuizSubscriber("Rion"));

        CountDownLatch wakeupManThread = new CountDownLatch(publisher.getNumberOfSubscribers());
        publisher.getSubscribers().forEach(subscriber -> ((QuizSubscriber) subscriber).setWakeupManThread(wakeupManThread));

        this.logger.info("start");
        String[] items = {"A", "B", "C"};
        for (String item : items) {
            publisher.submit(item);
        }

        boolean wakeup = wakeupManThread.await(100, TimeUnit.MILLISECONDS);
        Assertions.assertThat(wakeup).isFalse();

        publisher.close();
        wakeupManThread.await(); // 要 publish close 的時候才會呼叫 subscriber 的 onComplete

        this.logger.info("done");
    }

    /**
     * 展示 backpressure 的概念
     */
    @Test
    void backpressure() throws InterruptedException {
        Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int maxBufferCapacity = 2;
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>(executor, maxBufferCapacity);
        publisher.subscribe(new QuizSubscriber("Aery", 100));
        publisher.subscribe(new QuizSubscriber("Rion", 500));

        CountDownLatch wakeupManThread = new CountDownLatch(publisher.getNumberOfSubscribers());
        publisher.getSubscribers().forEach(subscriber -> ((QuizSubscriber) subscriber).setWakeupManThread(wakeupManThread));

        this.logger.info("start");
        String[] items = {"A", "B", "C", "D", "E", "F", "G"};
        for (String item : items) {
            publisher.offer(item, (subscriber, data) -> { // test target
                this.logger.info("dropping item : {}, subscriber: {}", data, subscriber);
                return true; // 根據 javadoc, 回傳 true 僅只會再嘗試一次
            });
        }

        publisher.close();
        wakeupManThread.await();

        this.logger.info("done");
    }

}
