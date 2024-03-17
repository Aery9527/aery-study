package org.aery.study.java.module.y;

import org.aery.study.java.module.api.TextGraphicsService;
import org.aery.study.java.module.utils.tool.SquareCalculator;
import org.aery.study.java.module.utils.vo.TextFrame;
import org.aery.study.java.module.utils.vo.TextGraphics;

public class YGraphicsService implements TextGraphicsService {

    @Override
    public TextGraphics draw(TextFrame frame) {
        int contentLength = SquareCalculator.findLength(frame.length(), frame.width());
        StringBuilder content = new StringBuilder();

        boolean isEven = contentLength % 2 == 0;
        int half = contentLength / 2 + 1;
        for (int i = 1; i <= contentLength; i++) {
            for (int j = 1; j <= contentLength; j++) {
                String s;
                if (i <= half) { // v
                    s = i == j || i + j - 1 == contentLength ? "*" : " ";
                } else { // | or ||
                    s = j == half || (isEven && j == half - 1) ? "*" : " ";
                }

                content.append(s);
            }

            if (i != contentLength) {
                content.append(System.lineSeparator());
            }
        }

        return new TextGraphics(
                frame,
                new TextFrame(contentLength, contentLength),
                content.toString());
    }

}
