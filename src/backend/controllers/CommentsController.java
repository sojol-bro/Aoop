package backend.controllers;

import backend.configs.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentsController {

    public static void addComment(int userId, int postId, String comment) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO comments (user_id, post_id, comment) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, postId);
            stmt.setString(3, comment);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getComments(int postId) {
        List<String> comments = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT c.comment, u.name, c.created_at FROM comments c JOIN users u ON c.user_id = u.id WHERE c.post_id = ? ORDER BY c.created_at ASC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String commentText = rs.getString("comment");
                String userName = rs.getString("name");
                String createdAt = rs.getString("created_at");
                comments.add(userName + " (" + createdAt + "): " + commentText);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public static int countComments(int postId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM comments WHERE post_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
