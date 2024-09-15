package org.aery.study.jdk15;

import java.util.function.Consumer;

public class JEP378_Text_Blocks {

    public static void main(String[] args) {
        Consumer<String> print = s -> {
            System.out.println(s);
            System.out.println("--------------------------------------------");
        };

        print.accept("Hello, \nWorld!");

        print.accept("""
                Hello, 
                World!
                """); // here has new line

        print.accept("""
                Hello, 
                \sWorld!
                """); // \s 是空白

        print.accept("""
                Hello, \
                World!
                """); // \ 防止換行
    }

}
