package org.aery.study.jdk21;

public class JEP440_Record_Patterns {

    record Point(int x, int y) {
    }

    record Line(Point start, Point end) {
    }

    record Pair<T>(T a, T b) {
    }

    public static void main(String[] args) {
        Object obj = null;

        if (obj instanceof Point(int x, int y)) { // 可以直接使用x, y
            System.out.println("x: " + x + ", y: " + y);
        }

        if (obj instanceof Line(Point(int x1, int y1), Point p)) { // 可以直接使用x1, y1, x2, y2
            System.out.println("Start: (" + x1 + "," + y1 + "), End: (" + p.x + "," + p.y + ")");
        }

        switch (obj) {
            case Point(int x, int y) -> System.out.println("x: " + x + ", y: " + y);
            case Line(Point(int x1, int y1), Point p) -> System.out.println("Start: (" + x1 + "," + y1 + "), End: (" + p.x + "," + p.y + ")");
            case Pair(Point p1, Line l1) -> System.out.println("Pair: " + p1 + ", " + l1); // 精確匹配
            case Pair(var p1, var l1) -> System.out.println("Pair: " + p1 + ", " + l1); // 模糊匹配, 可直接用來窮舉所有狀況
            default -> System.out.println("Unknown shape");
        }
    }

}
