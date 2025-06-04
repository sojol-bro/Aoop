package frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import backend.models.Notification;
import frontend.controllers.FriendsController;

import java.util.List;

public class NotificationControllerFx {

    @FXML private ListView<String> notificationList;
    private int currentUserId = 1;

    @FXML
    public void initialize() {
        List<Notification> notifications = NotificationController.getUnreadNotifications(currentUserId);
        for (Notification n : notifications) {
            notificationList.getItems().add(n.getMessage() + " [" + n.getCreatedAt() + "]");
            NotificationController.markAsRead(n.getId());
        }
    }
}
