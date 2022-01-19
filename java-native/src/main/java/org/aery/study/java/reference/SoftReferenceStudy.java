package org.aery.study.java.reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JvmInfo;

import java.lang.ref.SoftReference;

/**
 * 軟引用, 四種引用中強度次高.
 * 在memory夠用時不會被回收, 當不夠用時會優先回收這些軟引用物件, 當回收這些軟引用物件後還是不夠用JVM才會拋出OOM. <br/>
 */
public class SoftReferenceStudy {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftReferenceStudy.class); // slf4j

    public static void main(String[] args) {
        LOGGER.info("軟引用, memory夠用時發生GC不會被回收, 當不夠用時的GC才會被回收");
        LOGGER.info(JvmInfo.getJvmArguments().toString()); // -Xms5M -Xmx10M

        ReferenceTester<SoftReference<byte[]>> referenceTester = new ReferenceTester<>(
                "soft",
                SoftReference::new,
                SoftReference::get
        );

        referenceTester.test(10);
    }

}
