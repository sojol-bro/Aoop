package backend.controllers;

import backend.configs.DBConnection;

import java.sql.*;

public class LikesController {

    public static boolean toggleLike(int userId, int postId) {
        try (Connection conn = DBConnection.getConnection()) {
            // Check if already liked
            String checkSql = "SELECT * FROM likes WHERE user_id = ? AND post_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, postId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // If liked, unlike
                String deleteSql = "DELETE FROM likes WHERE user_id = ? AND post_id = ?";
                PreparedStatement delStmt = conn.prepareStatement(deleteSql);
                delStmt.setInt(1, userId);
                delStmt.setInt(2, postId);
                delStmt.executeUpdate();
                return false;  // Now unliked
            } else {
                // If not liked, insert like
                String insertSql = "INSERT INTO likes (user_id, post_id) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, postId);
                insertStmt.executeUpdate();
                return true;  // Now liked
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int countLikes(int postId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM likes WHERE post_id = ?";
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
