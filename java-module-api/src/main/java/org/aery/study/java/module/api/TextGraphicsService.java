package org.aery.study.java.module.api;

import org.aery.study.java.module.utils.vo.TextFrame;
import org.aery.study.java.module.utils.vo.TextGraphics;

import java.util.ServiceLoader;

public interface TextGraphicsService {

    int DEFAULT_WIDTH = 21;

    int DEFAULT_LENGTH = 11;

    static ServiceLoader<TextGraphicsService> load() {
        return ServiceLoader.load(TextGraphicsService.class);
    }

    default String name() {
        return getClass().getSimpleName();
    }

    default TextGraphics draw() {
        return draw(new TextFrame(DEFAULT_LENGTH, DEFAULT_WIDTH));
    }

    TextGraphics draw(TextFrame frame);

}
