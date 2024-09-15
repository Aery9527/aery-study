package org.aery.study.jdk16;

public class JEP390_Warnings_for_ValueBased_Classes {

    public static void main(String[] args) {
//        Integer value = new Integer(42); // 編譯時會有 warning
        Integer value = Integer.valueOf(42); // 建議使用這種方式建立

        synchronized (value) { // 編譯時會有 warning
            System.out.println("同步操作");
        }
    }

}
