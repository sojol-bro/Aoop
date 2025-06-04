package backend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class ChatController {

    @FXML private TextArea chatArea;
    @FXML private TextField messageField;

    private Session session;

    @FXML
    public void initialize() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://localhost:8025/ws/chat"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void sendMessage() {
        try {
            String message = messageField.getText();
            session.getBasicRemote().sendText(message);
            messageField.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        chatArea.appendText(message + "\n");
    }
}
