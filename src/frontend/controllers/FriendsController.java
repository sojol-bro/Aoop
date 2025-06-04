package frontend.controllers;
import frontend.controllers.FriendsController;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class FriendsController {

    @FXML private ListView<String> friendList;
    @FXML private ListView<String> pendingList;

    private int currentUserId = 1;

    @FXML
    public void initialize() {
        loadFriends();
        loadPending();
    }

    private void loadFriends() {
        List<String> friends = FriendController.getFriends(currentUserId);
        friendList.getItems().clear();
        for (String id : friends) {
            friendList.getItems().add("Friend ID: " + id);
        }
    }

    private void loadPending() {
        List<String> requests = FriendController.getPendingRequests(currentUserId);
        pendingList.getItems().clear();
        for (String id : requests) {
            pendingList.getItems().add("Request ID: " + id + " (Accept/Reject via code)");
        }
    }
}
