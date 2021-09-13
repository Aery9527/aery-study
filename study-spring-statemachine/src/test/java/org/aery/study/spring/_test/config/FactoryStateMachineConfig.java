package org.aery.study.spring._test.config;

import org.aery.study.spring.statmachine.Events;
import org.aery.study.spring.statmachine.States;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@TestConfiguration
@EnableStateMachineFactory
public class FactoryStateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
                .withStates()
                .initial(States.S1)
                .end(States.SF)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(States.SI).target(States.S1).event(Events.E1)
//                .action(action1())
                .and()
                .withExternal()
                .source(States.S1).target(States.S2).event(Events.E2);
    }

}
