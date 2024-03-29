package org.aery.study.spring.statmachine;

import org.aery.study.spring._test.config.FactoryStateMachineConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

@SpringBootTest(classes = {
        StateMachineApplication.class
        , FactoryStateMachineConfig.class
})
public class FactoryStateMachineApplicationTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StateMachineFactory<States, Events> factory;

    @Test
    public void test() {
        StateMachine<States, Events> stateMachine = this.factory.getStateMachine();
    }

}
