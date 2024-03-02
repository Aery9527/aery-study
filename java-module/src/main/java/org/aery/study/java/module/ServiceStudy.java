package org.aery.study.java.module;

import org.aery.study.java.module.api.TextGraphicsService;
import org.aery.study.java.module.utils.printer.ConsolePrinter;
import org.aery.study.java.module.utils.vo.TextGraphics;

import java.util.ServiceLoader;

public class ServiceStudy {

    public static void main(String[] args) {
        ConsolePrinter printer = ConsolePrinter.withFrame();
//        ConsolePrinter printer = ConsolePrinter.withoutFrame();

        ServiceLoader<TextGraphicsService> serviceLoader = TextGraphicsService.load();
        serviceLoader
                .stream() // 使用stream可以事先根據class篩選再實例化, 否則直接使用forEach即可
                .peek(provider -> System.out.println(provider.type()))
                .map(ServiceLoader.Provider::get)
                .forEach(service -> { // 實例化TextGraphicsService
                    System.out.println();
                    System.out.println(service.name());

                    TextGraphics graphics = service.draw();
//                    TextGraphics graphics = service.draw(new TextFrame(5, 5));

                    printer.print(graphics);
                });

    }

}
