package org.aery.study.jdk10;

import org.junit.jupiter.api.Test;

import java.util.List;

public class JEP286_Local_Variable_Type_Inference {

//    private var a = ""; // not allowed

    @Test
    void name() {
        var a = "";
        var b = List.of();
    }

//    private var metho1() {} // not allowed

//    private void metho2(var a) {} // not allowed

}
