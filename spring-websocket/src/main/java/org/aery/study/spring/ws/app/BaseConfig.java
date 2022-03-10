package org.aery.study.spring.ws.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import java.io.IOException;

public class BaseConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public String openUriWhenSpringReadied;

    public BaseConfig() {
    }

    public BaseConfig(String openUriWhenSpringReadied) {
        this.openUriWhenSpringReadied = openUriWhenSpringReadied;
    }

    @PostConstruct
    public void initial() {
        this.logger.info("use websocket config : " + getClass().getSimpleName());
    }

    @EventListener
    public boolean applicationReadyListener(ApplicationReadyEvent readyEvent) throws IOException {
        boolean openBrowser = false;

        if (this.openUriWhenSpringReadied == null || this.openUriWhenSpringReadied.isEmpty()) {
            return openBrowser;
        }

        String os = System.getProperty("os.name").toLowerCase();
        this.logger.debug("OS : " + os);

        Runtime runtime = Runtime.getRuntime();
        if (os.contains("windows")) {
            runtime.exec("explorer " + this.openUriWhenSpringReadied);
            openBrowser = true;
        } else if (os.contains("mac")) {
            runtime.exec("open " + this.openUriWhenSpringReadied);
            openBrowser = true;
        }

        if (openBrowser) {
            this.logger.info("auto open browser with '" + this.openUriWhenSpringReadied + "' please wait for a while...");
        }

        return openBrowser;
    }

}
