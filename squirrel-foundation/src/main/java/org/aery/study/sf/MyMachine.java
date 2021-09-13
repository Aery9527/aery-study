package org.aery.study.sf;

import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

@StateMachineParameters(stateType = MyState.class, eventType = MyEvent.class, contextType = MyContext.class)
public class MyMachine extends AbstractUntypedStateMachine {

    protected void withinA(MyState from, MyState to, MyEvent event, MyContext context) {
        context.contextInner = "inner_" + from.toString();
        System.out.println("Transition from '" + from.name() + "' to '" + to.name() + "' on event '" + event.name() +
                "' with context '" + context.contextInner + "'.");
    }

    protected void toD(MyState from, MyState to, MyEvent event, MyContext context) {
        System.out.println("Transition toD success");
    }

    protected void log(MyState from, MyState to, MyEvent event, MyContext context) {
        System.out.println("Transition log success");
    }

}
