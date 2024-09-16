package org.aery.study.jdk18;

public class JEP420_Pattern_Matching_for_switch {

    static String formatShape(Object shape) {
        return switch (shape) {
            case String s -> "String: " + s;
            case Integer i -> "Integer: " + i;
            case Double d when d > 0 -> "Positive Double: " + d;
            default -> "Unknown type";
        };
    }

    public static void main(String[] args) {
        System.out.println(formatShape("Hello"));
        System.out.println(formatShape(10));
        System.out.println(formatShape(3.14));
    }

}
