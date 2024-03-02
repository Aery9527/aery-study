package org.aery.study.java.module.api.hidden;

public class HiddenInAPI {

    public final String publicField = "api hidden public field";

    private final String privateField = "api hidden private field";

    public String publicMethod() {
        return "api hidden public method";
    }

    private String privateMethod() {
        return "api hidden private method";
    }

}
