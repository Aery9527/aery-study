package org.aery.study.jdk11;

import java.util.function.BiConsumer;

public class JEP323_Local_Variable_Syntax_for_Lambda_Parameters {

    public static void main(String[] args) {
        var x = "";
        BiConsumer<String, Integer> consumer = (var y, var z) -> System.out.println(y + z);
//        BiConsumer<String, Integer> consumer = (var y, Integer z) -> System.out.println(y + z); // not allowed
    }

}
