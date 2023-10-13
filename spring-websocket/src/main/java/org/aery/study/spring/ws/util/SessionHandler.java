package org.aery.study.spring.ws.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class SessionHandler<SessionType> {

    private final Map<String, SessionType> onlineSessions = new ConcurrentHashMap<>();

    private final BiConsumer<SessionType, String> sendAction;

    public SessionHandler(BiConsumer<SessionType, String> sendAction) {
        this.sendAction = sendAction;
    }

    public void add(String userNick, SessionType session) {
        onlineSessions.put(userNick, session);
    }

    public void remove(String userNick) {
        onlineSessions.remove(userNick);
    }

    public void send(String name, String message) {
        SessionType session = onlineSessions.get(name);
        if (session != null) {
            send(session, message);
        }
    }

    public void sendToAll(String message) {
        onlineSessions.forEach((name, session) -> send(session, message));
    }

    private void send(SessionType session, String message) {
        this.sendAction.accept(session, message);
    }

}
