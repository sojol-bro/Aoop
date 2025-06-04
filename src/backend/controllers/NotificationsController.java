package backend.controllers;

import backend.configs.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationsController {

    public static void addNotification(int userId, String type, Integer referenceId, String content) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO notifications (user_id, type, reference_id, content) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, type);
            if (referenceId != null) {
                stmt.setInt(3, referenceId);
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, content);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getUserNotifications(int userId) {
        List<String> notifications = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, type, content, is_read, created_at FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String status = rs.getBoolean("is_read") ? "Read" : "Unread";
                notifications.add("[" + status + "] " + rs.getString("type") + ": " + rs.getString("content") + " at " + rs.getString("created_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

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

    public static int countUnread(int userId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = FALSE";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
