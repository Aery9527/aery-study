package org.aery.study.java.agent;

import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class PremainAgent {

    public static void premain(String agentArgument, Instrumentation instrument) {
        System.out.println("Java Agent Loaded!");
        printClassPath(PremainAgent.class.getClassLoader());
    }

    private static void printClassPath(ClassLoader classLoader) {
        ClassLoader parent = classLoader.getParent();
        if (parent != null) {
            printClassPath(parent);
        }

        URLClassLoader ucl = (URLClassLoader) classLoader;
        URL[] urls = ucl.getURLs();

        System.out.println();
        System.out.println("=================================================");
        System.out.println(classLoader + "(" + classLoader.getClass() + ")");
        System.out.println("=================================================");
        Arrays.asList(urls).forEach(System.out::println);
    }

}
