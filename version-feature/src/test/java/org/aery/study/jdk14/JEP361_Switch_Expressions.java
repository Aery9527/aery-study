package org.aery.study.jdk14;

public class JEP361_Switch_Expressions {

    public static void main(String[] args) {
        String day = "MONDAY";
        int numLetters = switch (day) {
            case "MONDAY", "FRIDAY", "SUNDAY" -> 6;
            case "TUESDAY" -> 7;
            case "THURSDAY", "SATURDAY" -> 8;
            case "WEDNESDAY" -> {
                int result = day.length();
                yield result;  // 使用 yield 返回結果
            }
            default -> throw new IllegalArgumentException("Invalid day: " + day);
        };
        System.out.println("Number of letters: " + numLetters);
    }

}
