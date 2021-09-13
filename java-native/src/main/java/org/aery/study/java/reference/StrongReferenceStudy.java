package org.aery.study.java.reference;

/**
 * 強引用, 四種引用強度最高.
 * 只要存在GC roots上, 就永遠不會被GC, 因此memory不夠用時JVM就會直接拋出OOM.
 */
public class StrongReferenceStudy {

    public static void main(String[] args) throws InterruptedException {
        StrongReferenceStudy strongReference = new StrongReferenceStudy();
        strongReference.test();

        Thread.sleep(9527 * 1000); // 模擬其他程式運行
    }

    /**
     * 只要該StrongReference的classloader所載入的所有物件不在GC roots上, 該classloader便會被回收, 而此物件也將相對不在GC roots上, 也將會被回收.
     */
    private static Object o1 = new Object(); //

    /**
     * 只要每個StrongReference的instance不在GC roots上, 則此物件也將被GC
     */
    private Object object = new Object();

    private void test() {
        // 此物件在離開此method後便無任何reference, GC便會被回收
        Object object = new Object();
    }

}
