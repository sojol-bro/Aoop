package backend.controllers;
import backend.configs.DBConnection;
import java.sql.*;

public class LikeController {

    public static boolean hasUserLikedPost(int userId, int postId) {
        String sql = "SELECT id FROM post_likes WHERE user_id = ? AND post_id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, postId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true if a like exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean likePost(int userId, int postId) {
        if (hasUserLikedPost(userId, postId)) return false;
        String sql = "INSERT INTO post_likes (post_id, user_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean unlikePost(int userId, int postId) {
        String sql = "DELETE FROM post_likes WHERE post_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getLikeCount(int postId) {
        String sql = "SELECT COUNT(*) AS total FROM post_likes WHERE post_id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
