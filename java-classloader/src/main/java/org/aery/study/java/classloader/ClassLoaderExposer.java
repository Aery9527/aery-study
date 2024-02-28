package org.aery.study.java.classloader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassLoaderExposer {

    public static void showSystemClassLoaderParent() {
        showParent("SystemClassLoader", ClassLoader.getSystemClassLoader());
    }

    public static void showParent(String title, ClassLoader targetClassLoader) {
        printlnWithJavaVersion("show ClassLoader structure start with \"" + title + "\"");

        Map<ClassLoader, List<Class<?>>> classLoaderMap = new LinkedHashMap<>();

        do {
            List<Class<?>> extendClasses = new ArrayList<>();

            Class<?> superClass = targetClassLoader.getClass().getSuperclass();
            do {
                extendClasses.add(superClass);
                superClass = superClass.getSuperclass();
            } while (superClass != null);

            classLoaderMap.put(targetClassLoader, extendClasses);

            targetClassLoader = targetClassLoader.getParent();
        } while (targetClassLoader != null);

        int parentMaxLength = classLoaderMap.keySet().stream()
                .mapToInt(cl -> cl.toString().length())
                .max()
                .getAsInt() + 1;
        int parentMaxLengthHalf1 = parentMaxLength / 2;
        int parentMaxLengthHalf2 = parentMaxLength % 2 == 1 ? parentMaxLengthHalf1 - 1 : parentMaxLengthHalf1;

        String format = "%-" + parentMaxLength + "s";
        String format1 = "%" + parentMaxLengthHalf1 + "s";
        String format2 = "%" + parentMaxLengthHalf2 + "s";

        AtomicInteger index = new AtomicInteger(0);

        classLoaderMap.forEach((parentClass, extendClasses) -> {
            String currentClass = parentClass.toString();

            Class<?> firstExtendClass = extendClasses.get(0);
            println(String.format(format + "extends %s", currentClass, firstExtendClass.getName()));

            boolean finalParentClassLoader = index.incrementAndGet() == classLoaderMap.size();

            for (int i = 1; i < extendClasses.size(); i++) {
                Class<?> extendClass = extendClasses.get(i);

                boolean finalExtendClass = i == extendClasses.size() - 1;
                String arrow = finalParentClassLoader ? " " : (finalExtendClass ? "V" : "|");
                String msg = String.format(format1, arrow);
                msg += String.format(format2, "") + "   └>   " + extendClass.getName();
                println(msg);
            }
        });
    }

    private static void printlnWithJavaVersion(Object msg) {
        String javaVersion = System.getProperties().getProperty("java.version");
        println("[v" + javaVersion + "] " + msg);
    }

    private static void println(Object msg) {
        System.out.println(msg);
    }

}
