package org.aery.study.feign.api;

public class FeignApiVo1 {

    private String a;

    private int b;

    @Override
    public String toString() {
        return FeignApiVo1.class + "{a=" + this.a + ", b=" + this.b + "}";
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}
