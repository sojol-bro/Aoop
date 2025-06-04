package frontend.controllers;

import backend.UserSearchController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import models.User;

import java.util.List;

public class UserSearchController {

    @FXML private TextField searchField;
    @FXML private ListView<String> resultsList;

    private List<User> lastResults;

    @FXML
    public void onSearchClicked() {
        String keyword = searchField.getText().trim();
        if (!keyword.isEmpty()) {
            lastResults = UserSearchController.searchUsers(keyword);
            resultsList.getItems().clear();

            for (User user : lastResults) {
                resultsList.getItems().add(user.getUsername() + " (" + user.getEmail() + ")");
            }

            resultsList.setOnMouseClicked(this::handleListClick);
        }
    }

    private void handleListClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            int index = resultsList.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < lastResults.size()) {
                User selectedUser = lastResults.get(index);
                openUserProfile(selectedUser.getId());
            }
        }
    }

    private void openUserProfile(int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProfileView.fxml"));
            Parent root = loader.load();
            ProfileController controller = loader.getController();
            controller.setUserId(userId);  // or viewOtherUserProfile(userId);
            Main.setScene(root); // assuming a helper method to swap scenes
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
