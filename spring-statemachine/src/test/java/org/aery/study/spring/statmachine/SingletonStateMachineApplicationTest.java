package org.aery.study.spring.statmachine;

import org.aery.study.spring._test.config.SingletonStateMachineConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineMessageHeaders;
import reactor.core.publisher.Mono;

@SpringBootTest(classes = {
        StateMachineApplication.class
        , SingletonStateMachineConfig.class
})
public class SingletonStateMachineApplicationTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StateMachine<States, Events> stateMachine;

    @Test
    public void test() {
        Message<Events> message = MessageBuilder
                .withPayload(Events.E1)
                .setHeader(StateMachineMessageHeaders.HEADER_DO_ACTION_TIMEOUT, 5000)
                .build();

        this.logger.info("1");
        this.stateMachine.sendEvent(Mono.just(message)).subscribe(); // 如果listener出錯, 這邊是不會中斷的
        this.logger.info("2");

        this.stateMachine.sendEvent(Mono.just(message)).subscribe();
    }

}
