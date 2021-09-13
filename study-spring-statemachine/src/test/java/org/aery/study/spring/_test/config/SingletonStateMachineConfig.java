package org.aery.study.spring._test.config;

import org.aery.study.spring.statmachine.Events;
import org.aery.study.spring.statmachine.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.Collections;
import java.util.EnumSet;

@TestConfiguration
@EnableStateMachine
public class SingletonStateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config)
            throws Exception {
        config
                .withConfiguration()
                .machineId("kerker")
                .autoStartup(true)
                .listener(listener1())
//                .listener(listener2())
        ;
    }

    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states)
            throws Exception {
        states
                .withStates()
                .initial(States.SI)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(States.SI).target(States.S1).event(Events.E1)
                .action(action1())
                .and()
                .withExternal()
                .source(States.S1).target(States.S2).event(Events.E2);
    }

//    @Bean
//    public CompositeStateMachineListener<States, Events> listener2() {
//        return new CompositeStateMachineListener<States, Events>() {
//        };
//    }

    @Bean
    public Action<States, Events> action1() {
        return new Action<States, Events>() {
            private final Logger logger = LoggerFactory.getLogger(getClass());

            @Override
            public void execute(StateContext<States, Events> context) {
                this.logger.info(context.toString());
            }
        };
    }

    @Bean
    public StateMachineListener<States, Events> listener1() {
        return new StateMachineListenerAdapter<States, Events>() {
            private final Logger logger = LoggerFactory.getLogger(getClass());

            private void log(String actionName, String msg) {
                log(actionName, msg, null);
            }

            private void log(String actionName, String msg, Exception e) {
                if (e == null) {
                    this.logger.info("[" + actionName + "] " + msg);
                } else {
                    this.logger.error("[" + actionName + "] " + msg, e);
                }
            }

            @Override
            public void stateChanged(State<States, Events> from, State<States, Events> to) {
                if (from == null) { // 初始狀態的時候會是null
                    log("stateChanged", "to " + to.getId());
                } else {
                    log("stateChanged", "from " + from.getId() + " to " + to.getId());
                }
            }

            @Override
            public void stateEntered(State<States, Events> state) {
//                Flux<StateMachineEventResult<States, Events>> a = state.sendEvent((Message<Events>) null);
//                boolean b = state.shouldDefer((Message<Events>) null);
//                boolean l = state.isSimple();
//                boolean m = state.isComposite();
//                boolean n = state.isOrthogonal();
//                boolean o = state.isSubmachineState();
//                States id = state.getId();
//                Mono<Void> c = state.exit((StateContext<States, Events>) null);
//                Mono<Void> d = state.entry((StateContext<States, Events>) null);
//                Collection<States> e = state.getIds();
//                Collection<State<States, Events>> f = state.getStates();
//                PseudoState<States, Events> g = state.getPseudoState();
//                Collection<Events> h = state.getDeferredEvents();
//                Collection<Function<StateContext<States, Events>, Mono<Void>>> i = state.getEntryActions();
//                Collection<Function<StateContext<States, Events>, Mono<Void>>> j = state.getStateActions();
//                Collection<Function<StateContext<States, Events>, Mono<Void>>> k = state.getExitActions();
//                state.addStateListener((StateListener<States, Events>) null);
//                state.removeStateListener((StateListener<States, Events>) null);
//                state.addActionListener((ActionListener<States, Events>) null);
//                state.removeActionListener((ActionListener<States, Events>) null);

                log("stateEntered", state.toString());
            }

            @Override
            public void stateExited(State<States, Events> state) {
                log("stateExited", state.getId().toString());
            }

            @Override
            public void eventNotAccepted(Message<Events> event) {
                log("stateExited", event.getPayload().toString());
            }

            @Override
            public void transition(Transition<States, Events> transition) {
                log("stateExited", transition.getName());
            }

            @Override
            public void transitionStarted(Transition<States, Events> transition) {
                log("stateExited", transition.getName());
            }

            @Override
            public void transitionEnded(Transition<States, Events> transition) {
                log("stateExited", transition.getName());
            }

            @Override
            public void stateMachineStarted(StateMachine<States, Events> stateMachine) {
                log("stateExited", stateMachine.getId());
            }

            @Override
            public void stateMachineStopped(StateMachine<States, Events> stateMachine) {
                log("stateExited", stateMachine.getId());
            }

            @Override
            public void stateMachineError(StateMachine<States, Events> stateMachine, Exception exception) {
                log("stateExited", stateMachine.getId(), exception);
            }

            @Override
            public void extendedStateChanged(Object key, Object value) {
                log("stateExited", Collections.singletonMap(key, value).toString());
            }

            @Override
            public void stateContext(StateContext<States, Events> stateContext) {
                log("stateContext", stateContext.toString());
            }
        };
    }

}
