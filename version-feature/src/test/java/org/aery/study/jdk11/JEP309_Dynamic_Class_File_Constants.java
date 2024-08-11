package org.aery.study.jdk11;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;

public class JEP309_Dynamic_Class_File_Constants {

    /**
     * chatGPT-4o provide
     */
    public static void main(String[] args) throws Throwable {
        CallSite callSite = new ConstantCallSite(MethodHandles.constant(String.class, "Hello, CONSTANT_Dynamic!"));
        String dynamicConstant = (String) callSite.dynamicInvoker().invoke();
        System.out.println(dynamicConstant); // 輸出 "Hello, CONSTANT_Dynamic!"
    }

}
