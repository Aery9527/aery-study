package org.aery.study.jdk11;

import jdk.jfr.Event;
import jdk.jfr.Label;

public class JEP328_Flight_Recorder extends Event {

    public static class MyEvent extends Event {
        @Label("Message")
        String message;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000);

        MyEvent event = new MyEvent();
        event.message = "kerker";
        event.commit();

        for (int i = 1; i <= 5; i++) {
            Thread.sleep(1000);
            System.out.println(i + " second...");
        }

        event.end();

        System.out.println("finish");
    }

}
