package org.aery.study.java.module;

import org.aery.study.java.module.utils.tool.PathUtil;

import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.nio.file.Paths;
import java.util.Set;

/**
 * dynamic load a module from other path
 */
public class ModuleLayerStudy_LoadPhantomModule {

    public static void main(String[] args) {
        String phantomPath = PathUtil.mixRootPath("java-module-phantom", "out", "production", "classes");
        ModuleFinder moduleFinder = ModuleFinder.of(Paths.get(phantomPath));

        ModuleLayer bootLayer = ModuleLayer.boot();
        Configuration bootConfig = bootLayer.configuration();

        String targetModule = "aery.study.java.module.phantom";

        // if first moduleFinder or parent(by bootConfig) can't find the module, then use the second moduleFinder
        Configuration config = bootConfig.resolve(moduleFinder, ModuleFinder.of(), Set.of(targetModule));

        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        ModuleLayer phantomModuleLayer = bootLayer.defineModulesWithOneLoader(config, systemClassLoader);

        System.out.println("phantomModuleLayer.toString()      : " + phantomModuleLayer);
        System.out.println("phantomModuleLayer.modules()       : " + phantomModuleLayer.modules());
        System.out.println("phantomModuleLayer.configuration() : " + phantomModuleLayer.configuration());
        System.out.println("phantomModuleLayer.parents()       : " + phantomModuleLayer.parents());
        System.out.println();

        Module phantomModule = phantomModuleLayer.modules().stream().findFirst().get();
        ModuleDescriptor phantomModuleDescriptor = phantomModule.getDescriptor();
        phantomModuleDescriptor.requires().forEach(requires -> {
            System.out.println("phantom module requires : " + requires.name());
        });
        System.out.println();

        // the phantom requires api-module is from bootLayer, just like classloader hierarchy
        String apiModule = "aery.study.java.module.api";
        Module apiModuleFromBoot = bootLayer.modules().stream()
                .filter(module -> module.getName().equals(apiModule))
                .findFirst().get();
        Module apiModuleFromPhantom = bootLayer.modules().stream()
                .filter(module -> module.getName().equals(apiModule))
                .findFirst().get();
        System.out.println("apiModuleFromBoot == apiModuleFromPhantom : " + (apiModuleFromBoot == apiModuleFromPhantom));
    }

}
