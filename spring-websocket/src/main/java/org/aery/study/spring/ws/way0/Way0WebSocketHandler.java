package org.aery.study.spring.ws.way0;

import org.aery.study.spring.ws.util.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Way0WebSocketHandler implements WebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String toSpecifiedUserKey = "user";

    private final String toSpecifiedUserMsg = "msg";

    private final Pattern toSpecifiedPattern = Pattern.compile("^to (?<" + toSpecifiedUserKey + ">[^:]*):(?<" + toSpecifiedUserMsg + ">.*)");

    @Autowired
    private SessionHandler<WebSocketSession> sessionHandler;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String path = session.getUri().getPath();
        String username = path.substring(1);

        session.getAttributes().put("userName", username);

        String message = "[" + username + "] 加入聊天室!";
        this.logger.info(message);
        this.sessionHandler.add(username, session);
        this.sessionHandler.sendToAll(message);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String name = session.getAttributes().get("userName").toString();
        TextMessage textMessage = (TextMessage) message;
        String payload = textMessage.getPayload();

        String user = null;
        String msg = null;

        Matcher matcher = this.toSpecifiedPattern.matcher(payload);
        if (matcher.find()) {
            user = matcher.group(this.toSpecifiedUserKey);
            msg = matcher.group(this.toSpecifiedUserMsg);
        } else {
            msg = payload;
        }

        String finalMsg;
        if (user == null) {
            finalMsg = "[" + name + "]：" + msg;
            this.sessionHandler.sendToAll(finalMsg);
        } else {
            finalMsg = "[" + name + "] to [" + user + "]：" + msg;

            this.sessionHandler.send(name, finalMsg); // to specify user
            if (!name.equals(user)) {
                this.sessionHandler.send(user, finalMsg); // to specify user
            }
        }

        this.logger.info(finalMsg);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        try {
            session.close();
        } catch (IOException e) {
            this.logger.error("", e);
        }
        this.logger.error("", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String name = session.getAttributes().get("userName").toString();
        String message = "[" + name + "] 離開聊天室!";
        this.logger.info(message);
        this.sessionHandler.remove(name);
        this.sessionHandler.sendToAll(message);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
        // 設為true時, handleMessage可能到並非一個完整訊息, 取決於底層也支持部分消息時
        // 主要是因為websocket傳輸協定裡FIN標誌(1bit), 0:表示後續還有消息, 1:表示後續沒有消息
//        return true;
    }

}
