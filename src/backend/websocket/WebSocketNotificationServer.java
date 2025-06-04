package backend.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/notifications")
public class WebSocketNotificationServer {

    // Map userId to session
    private static Map<Integer, Session> userSessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        // After opening, client should send userId as first message to register
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            // Expecting message like "REGISTER:<userId>"
            if (message.startsWith("REGISTER:")) {
                int userId = Integer.parseInt(message.substring(9));
                userSessions.put(userId, session);
                System.out.println("User " + userId + " registered for notifications");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        userSessions.values().remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    // Send notification to a user
    public static void sendNotification(int userId, String notificationMessage) {
        Session session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(notificationMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
