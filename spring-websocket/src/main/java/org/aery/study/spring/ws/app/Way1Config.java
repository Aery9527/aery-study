package org.aery.study.spring.ws.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.io.IOException;

/**
 * JSR356操作模式, 使用線上ws測試(http://www.websocket-test.com/)比較簡單.
 */
@ConditionalOnProperty(name = SpringWebsocketApplication.WS_USE_WAY, havingValue = "1")
@Configuration
@EnableWebSocket
@ComponentScan("org.aery.study.spring.ws.way1")
public class Way1Config extends BaseConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Way1Config() {
        super("http://www.websocket-test.com/");
    }

    @Override
    public boolean applicationReadyListener(ApplicationReadyEvent readyEvent) throws IOException {
        boolean openBrowser = super.applicationReadyListener(readyEvent);

        if (openBrowser) {
            this.logger.info("you can use \"ws://localhost:8080/ws/Rion\" to connect websocket.");
        }

        this.logger.info("[SPECIAL MESSAGE PREFIX] \"to <USER_NAME>:<MESSAGE>\" can send message to specify user.");
        this.logger.info("[SPECIAL MESSAGE PREFIX] EX: \"to Aery:9527\" then \"Aery\" will receive \"9527\" if existed.");

        return openBrowser;
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
