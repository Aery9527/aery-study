package org.aery.study.java.reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ReferenceTester<Type> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReferenceTester.class); // slf4j

    public static final int _1M = 1024 * 1024;

    private final List<Object> strongList = new ArrayList<>();

    private final List<Object> referenceList = new ArrayList<>();

    private final String strongName = "strong";

    private final String name;

    private final String format;

    private final Function<byte[], Type> setter;

    private final Function<Type, byte[]> getter;

    private int restMs = 0;

    private Runnable stopAction = () -> {
    };

    public ReferenceTester(String name, Function<byte[], Type> setter, Function<Type, byte[]> getter) {
        this.name = name;
        this.setter = setter;
        this.getter = getter;

        this.format = "(%s) %-12s| ";
    }

    public void test(int times) {
        try {
            Runnable restAction;
            if (this.restMs <= 0) {
                restAction = () -> {
                };
            } else {
                restAction = () -> {
                    try {
                        Thread.sleep(this.restMs);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };
            }

            for (int index = 1; index <= times; index++) {
                byte[] buff = new byte[_1M];

                boolean isStrongReference = isStrongReference(index);
                if (isStrongReference) {
                    strongList.add(buff);
                }

                Type reference = this.setter.apply(buff);
                LOGGER.info("create " + reference.toString());
                referenceList.add(reference);

                System.gc(); // 主動通知GC

                printReferenceList();

                restAction.run();
            }

        } finally {
            this.stopAction.run();
        }
    }

    @SuppressWarnings("unchecked")
    private void printReferenceList() {
        StringBuilder msg = new StringBuilder();
        for (int index = 1; index <= this.referenceList.size(); index++) {
            Object object = this.referenceList.get(index - 1);

            boolean isStrongReference = isStrongReference(index);
            String name = isStrongReference ? this.strongName : this.name;

            object = this.getter.apply((Type) object);
            msg.append(String.format(this.format, name, object));
        }
        LOGGER.info(msg.toString());
    }

    private boolean isStrongReference(int index) {
        return index == 1;
    }

    public int getRestMs() {
        return restMs;
    }

    public void setRestMs(int restMs) {
        this.restMs = restMs;
    }

    public Runnable getStopAction() {
        return stopAction;
    }

    public void setStopAction(Runnable stopAction) {
        this.stopAction = stopAction;
    }

}
