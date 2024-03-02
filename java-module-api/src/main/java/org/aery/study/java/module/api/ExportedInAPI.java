package org.aery.study.java.module.api;

public class ExportedInAPI {

    public final String publicField = "api exported public field";

    private final String privateField = "api exported private field";

    public String publicMethod() {
        return "api exported public method";
    }

    private String privateMethod() {
        return "api exported private method";
    }

}
