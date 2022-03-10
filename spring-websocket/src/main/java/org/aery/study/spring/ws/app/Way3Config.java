package org.aery.study.spring.ws.app;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

/**
 * 點對點模式 (引入Spring Security)
 * 參考 http://www.mydlq.club/article/86/#%E5%8D%81springboot-%E5%AE%9E%E7%8E%B0-websocket-%E7%A4%BA%E4%BE%8B%E4%BA%8C%E5%AE%9E%E7%8E%B0%E7%82%B9%E5%AF%B9%E7%82%B9%E6%A8%A1%E5%BC%8F%E5%BC%95%E5%85%A5-spring-security-%E5%AE%9E%E7%8E%B0%E9%89%B4%E6%9D%83
 */
@ConditionalOnProperty(name = SpringWebsocketApplication.WS_USE_WAY, havingValue = "3")
@Configuration
@EnableWebSocketMessageBroker
@ComponentScan("org.aery.study.spring.ws.way3")
public class Way3Config extends BaseConfig {

    public Way3Config() {
        super("http://localhost:8080/");
    }

}
