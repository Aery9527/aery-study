package org.aery.study.sf;

import org.squirrelframework.foundation.fsm.*;

public class Application {

    public static void main(String[] args) {
        //构造状态机 参数分别是T S E C
        StateMachineBuilder builder = StateMachineBuilderFactory.create(MyMachine.class, MyState.class, MyEvent.class, MyContext.class);

        //流式API使用
        //1.创建一个状态从状态A到B，并且MyEvent.GoToB事件触发的external transition。
        builder.externalTransition().from(MyState.A).to(MyState.B).on(MyEvent.ToB);

        //2.创建一个优先级为TransitionPriority.HIGH，内部状态为’A’，当事件为’WithinA’便执行’myAction’的状态机。
        //internal transition意思是transition完成之后，没有状态退出和进入。优先级被用来覆盖继承来的状态机中的transition。
        builder.internalTransition(TransitionPriority.HIGH).within(MyState.A).on(MyEvent.WithinA).callMethod("withinA");

        //3.创建一个优先级为TransitionPriority.HIGH，内部状态为’A’，当事件为’WithinA’便执行’myAction’的状态机。
        // internal transition意思是transition完成之后，没有状态退出和进入。优先级被用来覆盖继承来的状态机中的transition。
        builder.externalTransition().from(MyState.C).to(MyState.D).on(MyEvent.ToD).when(new Condition<MyContext>() {
            public boolean isSatisfied(MyContext context) {
                return false;
            }

            public String name() {
                return "condition_toD";
            }
        }).callMethod("toD");

        //any 任意 from to on 均可定义 any
        builder.transit().fromAny().toAny().onAny().callMethod("log");
        // 创建一个状态机实例。注意，一旦通过builder创建好状态机，该builder就不能再用于定义任何包含新元素的状态机。
        UntypedStateMachine fsm = ((UntypedStateMachineBuilder) builder).newUntypedStateMachine(MyState.A);

        System.out.println("Current1 state is " + fsm.getCurrentState());//使用懒加载，打印bull
        //fsm.fire(MyEvent.ToB);
        //System.out.println("Current2 state is "+fsm.getCurrentState());
        //第一个参数 event，第二个参数，上线文类型
        fsm.fire(MyEvent.WithinA, new MyContext());//进入 MyMachine withinA方法

        System.out.println("Current3 state is " + fsm.getCurrentState());
    }

}
