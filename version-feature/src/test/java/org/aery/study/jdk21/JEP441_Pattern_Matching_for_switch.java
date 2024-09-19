package org.aery.study.jdk21;

public class JEP441_Pattern_Matching_for_switch {

    static String formatShape(Object shape) {
        return switch (shape) {
            case null -> "Null";
            case String s -> "String: " + s;
            case Integer i -> "Integer: " + i;
            case Double d when d > 0 -> "Positive Double: " + d; // and
            default -> "Unknown type";
        };
    }

    public static void main(String[] args) {
        System.out.println(formatShape("Hello"));
        System.out.println(formatShape(10));
        System.out.println(formatShape(3.14));
        System.out.println((Object) null);
    }

}
