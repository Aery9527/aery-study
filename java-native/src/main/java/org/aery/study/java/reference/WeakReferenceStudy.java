package org.aery.study.java.reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JvmInfo;

import java.lang.ref.WeakReference;

/**
 * 弱引用, 四種引用中強度次弱.
 * 只要有GC就必定會被回收.
 */
public class WeakReferenceStudy {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeakReferenceStudy.class); // slf4j

    public static void main(String[] args) {
        LOGGER.info("弱引用, 任何時候GC發生時就會被回收");
        LOGGER.info(JvmInfo.getJvmArguments().toString()); // -Xms5M -Xmx10M

        ReferenceTester<WeakReference<byte[]>> referenceTester = new ReferenceTester<>(
                "weak",
                WeakReference::new,
                WeakReference::get
        );

        referenceTester.test(10);
    }

}
