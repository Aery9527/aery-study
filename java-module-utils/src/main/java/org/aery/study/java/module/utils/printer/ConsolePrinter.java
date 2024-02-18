package org.aery.study.java.module.utils.printer;

import org.aery.study.java.module.utils.printer.impl.ConsoleFramePrinter;
import org.aery.study.java.module.utils.printer.impl.ConsolePurePrinter;
import org.aery.study.java.module.utils.vo.TextGraphics;

public interface ConsolePrinter {

    static ConsolePrinter withoutFrame() {
        return new ConsolePurePrinter();
    }

    static ConsolePrinter withFrame() {
        return new ConsoleFramePrinter();
    }

    void print(TextGraphics graphics);

}
