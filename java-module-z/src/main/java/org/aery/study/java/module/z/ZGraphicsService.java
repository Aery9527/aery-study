package org.aery.study.java.module.z;

import org.aery.study.java.module.utils.tool.SquareCalculator;
import org.aery.study.java.module.api.TextGraphicsService;
import org.aery.study.java.module.utils.vo.TextFrame;
import org.aery.study.java.module.utils.vo.TextGraphics;

public class ZGraphicsService implements TextGraphicsService {

    @Override
    public TextGraphics draw(TextFrame frame) {
        int contentLength = SquareCalculator.findLength(frame.length(), frame.width());
        StringBuilder content = new StringBuilder();

        for (int i = 1; i <= contentLength; i++) {
            for (int j = 1; j <= contentLength; j++) {
                String s = i == 1 || i == contentLength || i + j - 1 == contentLength ? "*" : " ";
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
