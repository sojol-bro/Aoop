package backend.websocket;

import backend.controllers.FriendController;
import backend.websocket.ChatServer;
import backend.websocket.WebSocketLauncher;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/chat")
public class WebSocketServer {
    private static final CopyOnWriteArraySet<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("New connection: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        // Placeholder logic for extracting sender/receiver from message
        String senderId = "1";
        String receiverId = "2";

        boolean areFriends = FriendController.getFriends(Integer.parseInt(senderId)).contains(receiverId);
        if (!areFriends) {
            session.getBasicRemote().sendText("You are not friends with this user.");
            return;
        }

        // Broadcast to all
        for (Session s : sessions) {
            s.getBasicRemote().sendText(message);
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("Connection closed: " + session.getId());
    }

    @ClientEndpoint
    public static class ChatClient {
        private Session session;
        private MessageHandler messageHandler;

        public ChatClient(String uri) {
            try {
                WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                container.connectToServer(this, new URI(uri));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @OnOpen
        public void onOpen(Session session) {
            this.session = session;
            System.out.println("Connected to chat server.");
        }

        @OnMessage
        public void onMessage(String message) {
            if (messageHandler != null) {
                messageHandler.handleMessage(message);
            }
        }

        public void sendMessage(String message) {
            try {
                if (session != null && session.isOpen()) {
                    session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void setMessageHandler(MessageHandler handler) {
            this.messageHandler = handler;
        }

        public interface MessageHandler {
            void handleMessage(String message);
        }
    }
}
