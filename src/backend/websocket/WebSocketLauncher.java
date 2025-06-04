package backend.websocket;

import jakarta.websocket.server.ServerEndpoint;
import org.glassfish.tyrus.server.Server;
import javax.websocket.*;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class WebSocketLauncher {
    public static void main(String[] args) {
        Server server = new Server("localhost", 8025, "/ws", WebSocketServer.class);

        try {
            server.start();
            System.out.println("WebSocket server started at ws://localhost:8025/ws/chat");
            System.in.read(); // Wait for user input to exit
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }

    @ServerEndpoint("/chat")
    public static class ChatServer {

        private static Set<Session> clients = new CopyOnWriteArraySet<>();

        @OnOpen
        public void onOpen(Session session) {
            clients.add(session);
            System.out.println("New connection: " + session.getId());
        }

        @OnMessage
        public void onMessage(String message, Session sender) {
            // Run the broadcasting in a separate thread
            new Thread(() -> {
                for (Session client : clients) {
                    try {
                        if (client.isOpen()) {
                            client.getBasicRemote().sendText(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @OnClose
        public void onClose(Session session) {
            clients.remove(session);
            System.out.println("Disconnected: " + session.getId());
        }

        @OnError
        public void onError(Session session, Throwable throwable) {
            System.err.println("Error on session " + session.getId());
            throwable.printStackTrace();
        }
    }
}
