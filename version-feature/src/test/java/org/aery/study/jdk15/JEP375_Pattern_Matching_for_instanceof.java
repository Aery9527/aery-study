package org.aery.study.jdk15;

public class JEP375_Pattern_Matching_for_instanceof {

    public static void main(String[] args) {
        Object obj = "Hello, World!";

        // 傳統的 instanceof 和強制轉型
        if (obj instanceof String) {
            String str = (String) obj;
            System.out.println(str.toUpperCase());
        }

        // 使用模式匹配
        if (obj instanceof String str) {
            System.out.println(str.toUpperCase());
        }
    }

}
