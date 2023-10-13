package org.aery.study.spring.ws.app;

import org.aery.study.spring.ws.util.SessionHandler;
import org.aery.study.spring.ws.way0.Way0WebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.io.IOException;

/**
 * 基於spring的{@link WebSocketConfigurer}的websocket server
 */
@ConditionalOnProperty(name = SpringWebsocketApplication.WS_USE_WAY, havingValue = "0")
@Configuration
@EnableWebSocket
@ComponentScan("org.aery.study.spring.ws.way0")
public class Way0Config extends BaseConfig implements WebSocketConfigurer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Way0Config() {
        super("http://www.websocket-test.com/");
    }

    @Override
    public boolean applicationReadyListener(ApplicationReadyEvent readyEvent) throws IOException {
        boolean openBrowser = super.applicationReadyListener(readyEvent);

        if (openBrowser) {
            this.logger.info("you can use \"ws://localhost:8080/Rion\" to connect websocket.");
        }

        this.logger.info("[SPECIAL MESSAGE PREFIX] \"to <USER_NAME>:<MESSAGE>\" can send message to specify user.");
        this.logger.info("[SPECIAL MESSAGE PREFIX] EX: \"to Aery:9527\" then \"Aery\" will receive \"9527\" if existed.");

        return openBrowser;
    }

    @Bean
    public Way0WebSocketHandler way0WebSocketHandler() {
        return new Way0WebSocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        Way0WebSocketHandler way0WebSocketHandler = way0WebSocketHandler();
        WebSocketHandlerRegistration webSocketHandlerRegistration = registry.addHandler(way0WebSocketHandler, "/*");
        webSocketHandlerRegistration = webSocketHandlerRegistration.setAllowedOrigins("*");
//        SockJsServiceRegistration sockJsServiceRegistration = webSocketHandlerRegistration.withSockJS();
    }

    @Bean
    public SessionHandler<WebSocketSession> sessionHandler() {
        return new SessionHandler<>((session, message) -> {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
