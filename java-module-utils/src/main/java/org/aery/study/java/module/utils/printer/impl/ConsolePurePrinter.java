package org.aery.study.java.module.utils.printer.impl;

import org.aery.study.java.module.utils.printer.ConsolePrinter;
import org.aery.study.java.module.utils.tool.SquareCalculator;
import org.aery.study.java.module.utils.vo.TextGraphics;

public class ConsolePurePrinter implements ConsolePrinter {

    @Override
    public void print(TextGraphics graphics) {
        System.out.print(transform(graphics));
        System.out.println();
    }

    public String transform(TextGraphics graphics) {
        return SquareCalculator.fillSquare(graphics);
    }

}
