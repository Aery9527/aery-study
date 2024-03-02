package org.aery.study.java.module;

import org.aery.study.java.module.api.TextGraphicsService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ResourcesStudy {

    public static void main(String[] args) {
        BiFunction<Class<?>, String, InputStream> clazzResourceFetcher = Class::getResourceAsStream;
        BiFunction<Module, String, InputStream> moduleResourceFetcher = (module, name) -> {
            try {
                return module.getResourceAsStream(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        BiConsumer<String, InputStream> textReader = (title, inputStream) -> {
            String text = "not found";
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                text = new BufferedReader(inputStreamReader)
                        .lines()
                        .collect(Collectors.joining("\n"));
            }

            System.out.println(title + " : " + text);
        };

        String innerFileName = "inner_resource.txt";
        String rootFileName = "root_resource.txt"; // always can read other module root resource

        TextGraphicsService.load().forEach(service -> {
            Class<? extends TextGraphicsService> clazz = service.getClass();
            Module module = clazz.getModule();

            String clazzTitle = clazz.getSimpleName() + " Class  read ";
            String classRootFileName = "/" + rootFileName;
            textReader.accept(clazzTitle + innerFileName, clazzResourceFetcher.apply(clazz, innerFileName));
            textReader.accept(clazzTitle + classRootFileName, clazzResourceFetcher.apply(clazz, classRootFileName));

            String moduleTitle = clazz.getSimpleName() + " Module read ";
            String moduleXInnerFileName = "org/aery/study/java/module/x/" + innerFileName;
            String moduleZInnerFileName = "org/aery/study/java/module/z/" + innerFileName;
            textReader.accept(moduleTitle + moduleXInnerFileName, moduleResourceFetcher.apply(module, moduleXInnerFileName));
            textReader.accept(moduleTitle + moduleZInnerFileName, moduleResourceFetcher.apply(module, moduleZInnerFileName));
            textReader.accept(moduleTitle + rootFileName, moduleResourceFetcher.apply(module, rootFileName));

            System.out.println();
        });
    }

}
