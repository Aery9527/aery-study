package org.aery.study.spring.ws.way1;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Way1SessionHandler {

    private final static Map<String, Session> ONLINE_SESSIONS = new ConcurrentHashMap<>();

    public static void add(String userNick, Session session) {
        ONLINE_SESSIONS.put(userNick, session);
    }

    public static void remove(String userNick) {
        ONLINE_SESSIONS.remove(userNick);
    }

    public static void send(String name, String message) {
        Session session = ONLINE_SESSIONS.get(name);
        if (session != null) {
            send(session, message);
        }
    }

    public static void sendToAll(String message) {
        ONLINE_SESSIONS.forEach((name, session) -> send(session, message));
    }

    private static void send(Session session, String message) {
        RemoteEndpoint.Async async = session.getAsyncRemote();
        async.sendText(message);
    }

}
