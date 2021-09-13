package org.aery.study.rocketmq.producer;

import org.aery.study.rocketmq.Common;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class SyncProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncProducer.class);

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException {
        DefaultMQProducer producer = new DefaultMQProducer(Common.PRODUCER_GROUP);
        producer.setNamesrvAddr(Common.NAMESRV_ADDR);

        try {
            producer.start();

            byte[] body = "Hello RocketMQ".getBytes(RemotingHelper.DEFAULT_CHARSET);
            Message msg = new Message(Common.TOPIC_SYNC, body);

            SendResult sendResult = null;
            try {
                sendResult = producer.send(msg); // 發送當下即會等到broker響應結果
            } catch (RemotingException | MQBrokerException | InterruptedException e) {
                e.printStackTrace();
            }

            LOGGER.info("{}", sendResult);

        } finally {
            producer.shutdown();
        }
    }

}
