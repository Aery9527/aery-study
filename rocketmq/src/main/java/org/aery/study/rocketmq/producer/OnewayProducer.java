package org.aery.study.rocketmq.producer;

import org.aery.study.rocketmq.Common;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class OnewayProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(Common.PRODUCER_GROUP);
        producer.setNamesrvAddr(Common.NAMESRV_ADDR);

        try {
            producer.start();

            byte[] body = "Hello RocketMQ".getBytes(RemotingHelper.DEFAULT_CHARSET);
            Message msg = new Message(Common.TOPIC_ONEWAY, body);
            producer.sendOneway(msg); // 只管送出不管成功與否

        } finally {
            producer.shutdown();
        }
    }

}
