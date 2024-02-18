package org.aery.study.java.module;

import org.aery.study.java.module.api.TextGraphicsService;
import org.aery.study.java.module.utils.printer.ConsolePrinter;
import org.aery.study.java.module.utils.vo.TextGraphics;

public class Entry {

    public static void main(String[] args) {
        ConsolePrinter printer = ConsolePrinter.withFrame();
//        ConsolePrinter printer = ConsolePrinter.withoutFrame();

        Iterable<TextGraphicsService> serviceLoader = TextGraphicsService.load();
        serviceLoader.forEach(service -> {
            System.out.println();
            System.out.println(service.name());

            TextGraphics graphics = service.draw();
//        TextGraphics graphics = service.draw(new TextFrame(5, 5));

            printer.print(graphics);
        });
    }

}
