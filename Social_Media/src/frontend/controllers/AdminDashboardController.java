package controllers;

import config.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import java.sql.*;

public class AdminDashboardController {

    @FXML private TableView<String> userTable;
    @FXML private TableView<String> postTable;

    @FXML
    public void initialize() {
        loadUsers();
        loadPosts();
    }

    private void loadUsers() {
        ObservableList<String> data = FXCollections.observableArrayList();
        String sql = "SELECT id, name, email FROM users";

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                data.add(rs.getInt("id") + " | " + rs.getString("name") + " | " + rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ListView<String> listView = new ListView<>(data);
        userTable.setPlaceholder(listView);
    }

    private void loadPosts() {
        ObservableList<String> data = FXCollections.observableArrayList();
        String sql = "SELECT p.id, u.name, p.content FROM posts p JOIN users u ON p.user_id = u.id";

        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                data.add("Post #" + rs.getInt("id") + " by " + rs.getString("name") + ": " + rs.getString("content"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ListView<String> listView = new ListView<>(data);
        postTable.setPlaceholder(listView);
    }
}
