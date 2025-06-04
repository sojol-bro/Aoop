package backend.controllers;

import backend.configs.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationController {

    // Create notification
    public static void createNotification(int userId, String type, int relatedId, String content) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO notifications (user_id, type, related_id, content) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, type);
            stmt.setInt(3, relatedId);
            stmt.setString(4, content);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch unread notifications for user
    public static List<String> getUnreadNotifications(int userId) {
        List<String> notifications = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, type, content FROM notifications WHERE user_id = ? AND is_read = FALSE ORDER BY created_at DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add("[" + rs.getString("type") + "] " + rs.getString("content"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    // Mark notifications as read
    public static void markAsRead(int notificationId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE notifications SET is_read = TRUE WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, notificationId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
