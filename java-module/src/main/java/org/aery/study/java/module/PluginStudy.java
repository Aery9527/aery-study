package org.aery.study.java.module;

import org.aery.study.java.module.api.TextGraphicsService;
import org.aery.study.java.module.utils.printer.ConsolePrinter;
import org.aery.study.java.module.utils.tool.PathUtil;
import org.aery.study.java.module.utils.vo.TextGraphics;

import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.file.Paths;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * dynamic service loading with ServiceLoader from other module
 */
public class PluginStudy {

    public static void main(String[] args) {
//        ConsolePrinter printer = ConsolePrinter.withoutFrame();
        ConsolePrinter printer = ConsolePrinter.withFrame();

        ModuleLayer pluginLayer = loadPluginLayer(); // parent is boot layer, so there are included x,y modules
        ServiceLoader.load(pluginLayer, TextGraphicsService.class).forEach(service -> {
            System.out.println();
            System.out.println(service.name());

            TextGraphics graphics = service.draw();
//            TextGraphics graphics = service.draw(new TextFrame(21, 12));

            printer.print(graphics);
        });
    }

    public static ModuleLayer loadPluginLayer() {
        String moduleYPath = PathUtil.mixRootPath("java-module-y", "out", "production", "classes");
        ModuleFinder moduleFinder = ModuleFinder.of(Paths.get(moduleYPath));

        Set<ModuleReference> moduleReferences = moduleFinder.findAll();
        Set<String> pluginRoots = moduleReferences.stream()
                .map(ModuleReference::descriptor)
                .map(ModuleDescriptor::name)
                .collect(Collectors.toSet());

        ModuleLayer bootLayer = ModuleLayer.boot();
        Configuration bootConfig = bootLayer.configuration();

        // different from ModuleLayerStudy_LoadPhantomModule use resolve to load module,
        // here we use resolveAndBind to load module with service,
        // so no need specify module name be a root module
        Configuration config = bootConfig.resolveAndBind(moduleFinder, ModuleFinder.of(), pluginRoots);

        return bootLayer.defineModulesWithOneLoader(config, ClassLoader.getSystemClassLoader());
    }

}
