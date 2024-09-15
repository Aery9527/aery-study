package org.aery.study.jdk17;

public class JEP409_Sealed_Classes {

    public abstract sealed class Shape implements Cloneable permits Circle, Square, Triangle {
    }

    public final class Circle extends Shape { // must be final
    }

    public final class Square extends Shape { // must be final
    }

    public non-sealed class Triangle extends Shape { // can be extended
    }

    public class IsoscelesTriangle extends Triangle {
    }

}
