package org.aery.study.jdk11;

import jdk.jfr.Event;
import jdk.jfr.Label;

public class JEP328_Flight_Recorder extends Event {

    public static class MyEvent extends Event {
        @Label("Message")
        String message;
    }

    public static void main(String[] args) {
        MyEvent event = new MyEvent();
        event.message = "kerker";
        event.commit();
    }

}
