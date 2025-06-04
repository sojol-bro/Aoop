package frontend.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class NotificationController {

    @FXML
    private VBox notificationPane;

    @FXML
    private ListView<String> notificationList;

    @FXML
    private Label notificationBadge;

    private Session session;
    private int userId; // Set this after login

    public void initialize() {
        connectToNotificationServer();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private void connectToNotificationServer() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            URI uri = new URI("ws://localhost:8080/notifications");
            container.connectToServer(this, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to Notification Server");
        try {
            // Register userId with server
            session.getBasicRemote().sendText("REGISTER:" + userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message) {
        Platform.runLater(() -> {
            notificationList.getItems().add(message);
            int count = notificationList.getItems().size();
            notificationBadge.setText(String.valueOf(count));
            notificationBadge.setVisible(true);
        });
    }

    @OnClose
    public void onClose() {
        System.out.println("Disconnected from Notification Server");
    }

    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    // Optional: Clear notifications and badge
    @FXML
    public void clearNotifications() {
        notificationList.getItems().clear();
        notificationBadge.setVisible(false);
    }
}
