package frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import backend.models.User;
import backend.controllers.UserController;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = UserController.login(username, password);

        if (user != null) {
            statusLabel.setText("Login successful!");
            loadChatPage();
        } else {
            statusLabel.setText("Invalid credentials.");
        }
    }

    @FXML
    public void handleRegister() {
        boolean registered = UserController.register(usernameField.getText(), passwordField.getText());
        statusLabel.setText(registered ? "Registered successfully!" : "Registration failed.");
    }

    private void loadChatPage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/frontend/src/views/chat.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
