package org.aery.study.java.module;

import org.aery.study.java.module.api.TextGraphicsService;
import org.aery.study.java.module.utils.printer.ConsolePrinter;

import java.lang.module.ModuleDescriptor;
import java.util.Objects;

public class ModuleStudy {

    @Deprecated
    public static void main(String[] args) {
        Module currentModule = ModuleStudy.class.getModule();
        ModuleLayer currentModuleLayer = currentModule.getLayer();
        ModuleDescriptor currentModuleDescriptor = currentModule.getDescriptor();

        println("ModuleStudy-Module.getDescriptor()  : " + currentModuleDescriptor);
        println("ModuleStudy-Module.getLayer()       : " + currentModuleLayer);
        println("ModuleStudy-Module.getName()        : " + currentModule.getName());
        println();

        Module apiModule = TextGraphicsService.class.getModule();
        Module utilsModule = ConsolePrinter.class.getModule();
        Module xModule = ModuleLayer.boot().findModule("aery.study.java.module.x").get();
        Module zModule = ModuleLayer.boot().findModule("aery.study.java.module.z").get();

        showModuleDescriptor(currentModuleDescriptor);
        showModuleDescriptor(apiModule.getDescriptor());
        showModuleDescriptor(utilsModule.getDescriptor());
        showModuleDescriptor(xModule.getDescriptor());
        showModuleDescriptor(zModule.getDescriptor());

        showModuleLayer("ModuleLayer.boot()", ModuleLayer.boot());
        showModuleLayer("currentModuleLayer", currentModuleLayer);
        showModuleLayer("apiModule", apiModule.getLayer());
        showModuleLayer("utilsModule", utilsModule.getLayer());
        showModuleLayer("xModule", xModule.getLayer());
        showModuleLayer("zModule", zModule.getLayer());

        currentModule.addExports("org.aery.study.java.module", xModule); // export exported package
        currentModule.addExports("org.aery.study.java.module.other", zModule); // export not exported modules

        wrapException(() -> xModule.addExports("org.aery.study.java.module.x", currentModule)); // can't operate other module
        wrapException(() -> zModule.addExports("org.aery.study.java.module.non", currentModule)); // can't export non-exist package, even package not exist
    }

    private static void showModuleDescriptor(ModuleDescriptor moduleDescriptor) {
        println("-------------------------------------");
        println("moduleDescriptor.name()             : " + moduleDescriptor.name());
        println("moduleDescriptor.version()          : " + moduleDescriptor.version());
        println("moduleDescriptor.toNameAndVersion() : " + moduleDescriptor.toNameAndVersion());
        println("moduleDescriptor.rawVersion()       : " + moduleDescriptor.rawVersion());
        println("moduleDescriptor.mainClass()        : " + moduleDescriptor.mainClass());
        println("moduleDescriptor.packages()         : " + moduleDescriptor.packages());
        println("moduleDescriptor.exports()          : " + moduleDescriptor.exports());
        println("moduleDescriptor.requires()         : " + moduleDescriptor.requires());
        println("moduleDescriptor.provides()         : " + moduleDescriptor.provides());
        println("moduleDescriptor.uses()             : " + moduleDescriptor.uses());
        println("moduleDescriptor.opens()            : " + moduleDescriptor.opens());
        println("moduleDescriptor.isOpen()           : " + moduleDescriptor.isOpen());
        println("moduleDescriptor.accessFlags()      : " + moduleDescriptor.accessFlags());
        println("moduleDescriptor.isAutomatic()      : " + moduleDescriptor.isAutomatic());
        println("moduleDescriptor.modifiers()        : " + moduleDescriptor.modifiers());
        println("-------------------------------------");
        println();
    }

    private static void showModuleLayer(String from, ModuleLayer moduleLayer) {
        println("----------------------------------");
        println("moduleLayer from            : " + from + " (" + Objects.toIdentityString(moduleLayer) + ")");
        println("moduleLayer.toString()      : " + moduleLayer);
        println("moduleLayer.modules()       : " + moduleLayer.modules());
        println("moduleLayer.configuration() : " + moduleLayer.configuration());
        println("moduleLayer.parents()       : " + moduleLayer.parents());
        println("----------------------------------");
        println();
    }

    private static void println() {
        println("");
    }

    private static void println(Object msg) {
        System.out.println(msg);
    }

    private static void wrapException(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
