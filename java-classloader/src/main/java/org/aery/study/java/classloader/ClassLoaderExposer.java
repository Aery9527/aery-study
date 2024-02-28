package org.aery.study.java.classloader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class ClassLoaderExposer {

    public static void showSystemClassLoaderStructure() {
        showStructure("SystemClassLoader", ClassLoader.getSystemClassLoader());
    }

    public static void showStructure(String title, ClassLoader targetClassLoader) {
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
                String arrow = finalExtendClass ? "V" : (finalParentClassLoader ? "!" : "|");
                String msg = String.format(format1, arrow);
                msg += String.format(format2, "") + "   └>   " + extendClass.getName();
                println(msg);
            }
        });

        String bootstrapName = "BootstrapClassLoader";
        int bootstrapNameHalfLength = bootstrapName.length() / 2;
        println(String.format("%" + (bootstrapNameHalfLength + parentMaxLengthHalf1) + "s", bootstrapName));
    }

    public static void showSystemClassLoaderBaseInfo() {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        do {
            showBaseInfo(cl);
            println();
            cl = cl.getParent();
        } while (cl != null);
    }

    public static void showBaseInfo(ClassLoader cl) {
        printlnWithJavaVersion("show ClassLoader base info with \"" + cl + "\"");

        Map<String, Object> info = new LinkedHashMap<>();
        info.put("getName()", cl.getName());

        String getDefinedPackages = "getDefinedPackages()";
        Package[] definedPackages = cl.getDefinedPackages();
        info.put(getDefinedPackages + ".length", definedPackages.length);
        for (int i = 0; i < definedPackages.length; i++) {
            Package definedPackage = definedPackages[i];
            info.put(getDefinedPackages + "[" + i + "]", definedPackage);
        }

        String getUnnamedModule = "getUnnamedModule()";
        Module unnamedModule = cl.getUnnamedModule();
        info.put(getUnnamedModule, unnamedModule);
        info.put(getUnnamedModule + ".getName()", unnamedModule.getName());
        info.put(getUnnamedModule + ".getLayer()", unnamedModule.getLayer());
        info.put(getUnnamedModule + ".Descriptor()", unnamedModule.getDescriptor());

        int keyMaxLength = info.keySet().stream()
                .mapToInt(String::length)
                .max()
                .getAsInt();

        Function<Object, String> format = msg -> String.format("%-" + keyMaxLength + "s ", msg);
        info.forEach((key, value) -> println(format.apply(key) + value));
    }

    private static void printlnWithJavaVersion(Object msg) {
        String javaVersion = System.getProperties().getProperty("java.version");
        println("[v" + javaVersion + "] " + msg);
    }

    private static void println() {
        println("");
    }

    private static void println(Object msg) {
        System.out.println(msg);
    }

}
