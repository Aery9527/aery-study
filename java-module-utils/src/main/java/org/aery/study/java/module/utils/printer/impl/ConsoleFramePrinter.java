package org.aery.study.java.module.utils.printer.impl;

import org.aery.study.java.module.utils.printer.ConsolePrinter;
import org.aery.study.java.module.utils.vo.TextFrame;
import org.aery.study.java.module.utils.vo.TextGraphics;

public class ConsoleFramePrinter implements ConsolePrinter {

    private final ConsolePurePrinter purePrinter = new ConsolePurePrinter();

    @Override
    public void print(TextGraphics graphics) {
        String content = purePrinter.transform(graphics);

        TextFrame boundaryFrame = graphics.boundaryFrame();
        String boundary = "+" + "-".repeat(boundaryFrame.width()) + "+";

        content = content.replaceAll(System.lineSeparator(), "|" + System.lineSeparator() + "|");
        content = boundary + System.lineSeparator() + "|" + content;
        content = content + "|" + System.lineSeparator() + boundary;

        System.out.print(content);
        System.out.println();
    }

}
