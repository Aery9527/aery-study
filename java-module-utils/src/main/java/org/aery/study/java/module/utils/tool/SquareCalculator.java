package org.aery.study.java.module.utils.tool;

import org.aery.study.java.module.utils.vo.TextFrame;
import org.aery.study.java.module.utils.vo.TextGraphics;

public class SquareCalculator {

    public static int findLength(int length, int width) {
        return Math.min(length, width);
    }

    public static String fillSquare(TextGraphics graphics) {
        TextFrame boundaryFrame = graphics.boundaryFrame();
        TextFrame contentFrame = graphics.contentFrame();
        String content = graphics.content();

        int startPrintIndex = (boundaryFrame.width() - contentFrame.width()) / 2;
        String filled = " ".repeat(startPrintIndex);

        content = filled + content;
        content = content.replaceAll(System.lineSeparator(), filled + System.lineSeparator() + filled);
        content = content + filled;
        return content;
    }

}
