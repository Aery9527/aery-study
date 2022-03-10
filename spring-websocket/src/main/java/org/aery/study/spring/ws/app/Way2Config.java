package org.aery.study.spring.ws.app;

import org.aery.study.spring.ws.way2.Way2PathConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 廣播模式
 * 參考 https://matthung0807.blogspot.com/2019/04/spring-boot-websocket_27.html
 */
@ConditionalOnProperty(name = SpringWebsocketApplication.WS_USE_WAY, havingValue = "2")
@Configuration
@EnableWebSocketMessageBroker
@ComponentScan("org.aery.study.spring.ws.way2")
public class Way2Config extends BaseConfig implements WebSocketMessageBrokerConfigurer {

    public Way2Config() {
        super("http://localhost:8080/");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry endpointRegistry) {
        endpointRegistry.addEndpoint(Way2PathConstant.ENDPOINT).withSockJS(); // 註冊一個給Client連至WebSocket Server的節點(websocket endpoint)
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry brokerRegistry) {
        brokerRegistry.enableSimpleBroker(Way2PathConstant.TOPIC); // 啟用一個訊息代理並設定訊息發送目地的前綴路徑
        brokerRegistry.setApplicationDestinationPrefixes(Way2PathConstant.PREFIX); // 設定導向至訊息處理器的前綴路徑
    }

}
