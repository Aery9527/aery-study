package org.aery.study.java.reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JvmInfo;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

/**
 * 虛引用, 四種引用中最弱, 一定要搭配ReferenceQueue操作.
 */
public class PhantomReferenceStudy {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhantomReferenceStudy.class); // slf4j

    private static final ReferenceQueue<byte[]> REFERENCE_QUEUE = new ReferenceQueue<>();

    private static volatile boolean IsRun = true;

    public static void main(String[] args) throws Exception {
        LOGGER.info("虛引用, 無法獲取真實的object, 用於關注該物件是否即將被回收用的情境");
        LOGGER.info(JvmInfo.getJVMArguments().toString()); // -Xms5M -Xmx10M

        pollingReferenceQueue();

        try {
            List<Object> list = new ArrayList<>();

            int times = 20;
            long restMs = 1000;
            for (int i = 1; i <= times; i++) {
                byte[] buff = new byte[ReferenceTester._1M];
                PhantomReference<byte[]> pr = new PhantomReference<>(buff, REFERENCE_QUEUE);
                list.add(pr); // 根據PhantomReference javadoc說明, 該物件必須有強引用, GC時才會進入referenceQueue

                System.gc();

                String msg = "(" + i + ") " + pr;
                LOGGER.info(msg);

                Thread.sleep(restMs);
            }

        } finally {
            IsRun = false;
        }
    }

    private static void pollingReferenceQueue() {
        new Thread(() -> {
            while (IsRun) {
                Reference<? extends byte[]> v = REFERENCE_QUEUE.poll();

                if (v == null) {
                    LOGGER.info(null);
                } else {
                    LOGGER.info("recycle " + v.toString());
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "polling").start();
    }

}
