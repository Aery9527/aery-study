package org.aery.study.rocketmq.consumer;

import org.aery.study.rocketmq.Common;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class Consumer {

    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Common.CONSUMER_GROUP);
        consumer.setNamesrvAddr(Common.NAMESRV_ADDR);

        consumer.subscribe(Common.TOPIC_SYNC, "*"); // subExpression用來過濾tag
        consumer.subscribe(Common.TOPIC_ASYNC, "*"); // subExpression用來過濾tag
        consumer.subscribe(Common.TOPIC_ONEWAY, "*"); // subExpression用來過濾tag

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; // 對broker標記該消息成功消費
        });

        consumer.start();

        System.out.printf("Consumer Started.%n");
    }

}
