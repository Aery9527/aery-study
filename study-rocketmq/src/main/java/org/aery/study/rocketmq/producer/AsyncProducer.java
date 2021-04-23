package org.aery.study.rocketmq.producer;

import org.aery.study.rocketmq.Common;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class AsyncProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncProducer.class);

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(Common.PRODUCER_GROUP);
        producer.setNamesrvAddr(Common.NAMESRV_ADDR);

        try {
            producer.start();

            producer.setRetryTimesWhenSendAsyncFailed(0);

            byte[] body = "Hello RocketMQ".getBytes(RemotingHelper.DEFAULT_CHARSET);
            Message msg = new Message(Common.TOPIC_ASYNC, body);

            CountDownLatch latch = new CountDownLatch(1);
            producer.send(msg, new SendCallback() { // 發送之後, broker響應結果由這邊處理
                @Override
                public void onSuccess(SendResult sendResult) {
                    LOGGER.info("{}", sendResult);
                    latch.countDown();
                }

                @Override
                public void onException(Throwable e) {
                    LOGGER.info("", e);
                    latch.countDown();
                }
            });
            latch.await(); // 若這邊不等, 則有可能producer還未跟broker離線就被關閉了

        } finally {
            producer.shutdown();
        }
    }

}
