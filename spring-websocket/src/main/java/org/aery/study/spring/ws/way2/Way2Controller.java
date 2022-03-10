package org.aery.study.spring.ws.way2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.*;

import java.util.Map;

@Controller
public class Way2Controller {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static class Constants {
        public static final String USER = "user";
    }

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MessageMapping("/join") // 從這個路徑收到的訊息會來到這個method
    @SendTo(Way2PathConstant.PUBLIC) // 返回的結果會送到這個路徑
    public Way2ChatMessage join(SimpMessageHeaderAccessor headerAccessor, @Payload Way2ChatMessage chatMessage) throws JsonProcessingException {
        printMessage(chatMessage);

        Map<String, Object> attributes = headerAccessor.getSessionAttributes();
        attributes.put(Constants.USER, chatMessage.getSender());

        return chatMessage;
    }

    @MessageMapping("/chat")
    @SendTo(Way2PathConstant.PUBLIC)
    public Way2ChatMessage chat(SimpMessageHeaderAccessor headerAccessor, @Payload Way2ChatMessage chatMessage) throws JsonProcessingException {
        printMessage(chatMessage);
        return chatMessage;
    }

    @EventListener
    public void connectEventListener(SessionConnectEvent event) throws JsonProcessingException {
        printMessage(event);
    }

    @EventListener
    public void connectedEventListener(SessionConnectedEvent event) throws JsonProcessingException {
        printMessage(event);
    }

    @EventListener
    public void disconnectEventListener(SessionDisconnectEvent event) throws JsonProcessingException {
        printMessage(event);

        // 建立一個user離開的訊息廣播給所有人
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> attributes = headerAccessor.getSessionAttributes();
        String user = (String) attributes.get(Constants.USER);

        Way2ChatMessage chatMessage = new Way2ChatMessage();
        chatMessage.setType(Way2ChatMessage.ChatType.LEAVE);
        chatMessage.setSender(user);
        messagingTemplate.convertAndSend(Way2PathConstant.PUBLIC, chatMessage);
    }

    @EventListener
    public void subscribeEventListener(SessionSubscribeEvent event) throws JsonProcessingException {
        printMessage(event);
    }

    @EventListener
    public void unsubscribeEventListener(SessionUnsubscribeEvent event) throws JsonProcessingException {
        printMessage(event);
    }

    private void printMessage(Object target) throws JsonProcessingException {
        StackTraceElement set = Thread.currentThread().getStackTrace()[2];
        String methodName = set.getMethodName();

        ObjectWriter prettyWriter = this.objectMapper.writerWithDefaultPrettyPrinter();
        String prettyJsonOfRequest = prettyWriter.writeValueAsString(target);
        this.logger.info("from [" + methodName + "] " + prettyJsonOfRequest);
    }

}
