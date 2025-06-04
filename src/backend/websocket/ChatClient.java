package backend.websocket;

import javafx.application.Platform;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class ChatClient {

    private Session session;
    private MessageHandler messageHandler;

    public ChatClient(URI serverEndpoint) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, serverEndpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to server");
    }

    @OnMessage
    public void onMessage(String message) {
        // Update UI in JavaFX Application Thread
        Platform.runLater(() -> {
            if (messageHandler != null) {
                messageHandler.handleMessage(message);
            }
        });
    }

    public void sendMessage(String message) {
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(message);
        }
    }

    public void setMessageHandler(MessageHandler handler) {
        this.messageHandler = handler;
    }

    public interface MessageHandler {
        void handleMessage(String message);
    }
}
