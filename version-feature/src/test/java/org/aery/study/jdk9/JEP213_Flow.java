package org.aery.study.jdk9;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

/**
 * {@link Flow}
 */
public class JEP213_Flow {

    public class QuizPublisher implements Flow.Publisher<Integer> {

        private final List<Flow.Subscriber<? super Integer>> subscribers = new ArrayList<>();

        @Override
        public void subscribe(Flow.Subscriber<? super Integer> subscriber) {
            this.subscribers.add(subscriber);
            subscriber.onSubscribe(new Quizubscription(subscriber));
        }

    }

    private class Quizubscription implements Flow.Subscription {
        private final Flow.Subscriber<? super Integer> subscriber;

        private boolean canceled = false;

        private Quizubscription(Flow.Subscriber<? super Integer> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            if (!canceled) {
                for (int i = 0; i < n; i++) {
                    subscriber.onNext(i);
                }
                subscriber.onComplete();
            }
        }

        @Override
        public void cancel() {
            canceled = true;
        }
    }

    public class QuizSubscriber implements Flow.Subscriber<Integer> {

        private final Logger logger = LoggerFactory.getLogger(getClass());

        private Flow.Subscription subscription;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(10); // 請求 10 個數據項
        }

        @Override
        public void onNext(Integer item) {
            this.logger.info("Got : " + item);
        }

        @Override
        public void onError(Throwable throwable) {
            this.logger.error("Error : " + throwable.getMessage());
        }

        @Override
        public void onComplete() {
            this.logger.info("Done!");
        }
    }

    @Test
    void flow_demo() {
        QuizPublisher publisher = new QuizPublisher();
        QuizSubscriber subscriber = new QuizSubscriber();
        publisher.subscribe(subscriber);
    }

}
