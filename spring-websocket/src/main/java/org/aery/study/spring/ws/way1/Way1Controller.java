package org.aery.study.spring.ws.way1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 這個方法操作起來相對複雜, 因為要自己管理session, 處理input/output格式等,
 * 而且真正接收到ws的實例是由container實例化的, 而且是每一個ws連接就會實例化一次,
 * 可以觀察{@link #Way1Controller}被建構時的stack, 就會很清楚知道差別了!
 * <p>
 * 也就是說在收到ws任何動作的時候, 當前實例不是透過spring所管理的實例,
 * 所以自然也就操作不到任何spring的東西, 只能透過static手段取得spring管理的實例,
 * 但操作static又有違spring IoC概念, 因此較建議採用其他由spring包裝好STOMP的操作方法.
 */
@Component // 但很鳥的是, 沒有讓spring scan這個class, endpoint又不會生效...
@ServerEndpoint(value = "/ws/{" + Way1Controller.NAME + "}")
public class Way1Controller {

    private static ApplicationContext APPLICATION_CONTEXT;

    public static final String NAME = "name";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String toSpecifiedUserKey = "user";

    private final String toSpecifiedUserMsg = "msg";

    private final Pattern toSpecifiedPattern = Pattern.compile("^to (?<" + toSpecifiedUserKey + ">[^:]*):(?<" + toSpecifiedUserMsg + ">.*)");

    private UserVerifier userVerifier;

    public Way1Controller() {
        StackTraceElement[] stes = Thread.currentThread().getStackTrace();
        StringBuilder stackInfo = new StringBuilder(Way1Controller.class.getSimpleName() + " construct!");
        Arrays.stream(stes).forEach(ste -> stackInfo.append(System.lineSeparator()).append("\t").append(ste));
        this.logger.debug(stackInfo.toString());

        if (APPLICATION_CONTEXT != null) { // 透過此方式獲得spring的bean
            this.userVerifier = APPLICATION_CONTEXT.getBean("userVerifier", UserVerifier.class);
        }
    }

    @EventListener
    public void applicationReadyListener(ApplicationReadyEvent readyEvent) {
        APPLICATION_CONTEXT = readyEvent.getApplicationContext(); // 必須透過static的方式留著spring的applicationContext
    }

    @OnOpen
    public void onOpen(@PathParam(value = NAME) String name, Session session) {
        this.userVerifier.verify("token"); // 驗證token, 以及取得user資訊

        String message = "[" + name + "] 加入聊天室!";
        this.logger.info(message);
        Way1SessionHandler.add(name, session);
        Way1SessionHandler.sendToAll(message);
    }

    @OnClose
    public void onClose(@PathParam(value = NAME) String name, Session session) {
        String message = "[" + name + "] 離開聊天室!";
        this.logger.info(message);
        Way1SessionHandler.remove(name);
        Way1SessionHandler.sendToAll(message);
    }

    @OnMessage
    public void OnMessage(@PathParam(value = NAME) String name, String message) {
        String user = null;
        String msg = null;

        Matcher matcher = toSpecifiedPattern.matcher(message);
        if (matcher.find()) {
            user = matcher.group(this.toSpecifiedUserKey);
            msg = matcher.group(this.toSpecifiedUserMsg);
        } else {
            msg = message;
        }

        String finalMsg;
        if (user == null) {
            finalMsg = "[" + name + "]：" + msg;
            Way1SessionHandler.sendToAll(finalMsg);
        } else {
            finalMsg = "[" + name + "] to [" + user + "]：" + msg;

            Way1SessionHandler.send(name, finalMsg); // to specify user
            if (!name.equals(user)) {
                Way1SessionHandler.send(user, finalMsg); // to specify user
            }
        }

        this.logger.info(finalMsg);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close(); // 這邊close之後會進到 @OnClose 方法
        } catch (IOException e) {
            this.logger.error("", e);
        }
        this.logger.error("", throwable);
    }

}
