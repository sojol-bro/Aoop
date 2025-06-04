package backend.controllers;

import backend.models.Comment;
import backend.configs.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentController {

    public static boolean addComment(int postId, int userId, String commentText) {
        String sql = "INSERT INTO comments (post_id, user_id, comment_text) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.setString(3, commentText);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Comment> getCommentsByPostId(int postId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE post_id = ? ORDER BY commented_at ASC";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Comment c = new Comment(
                        rs.getInt("id"),
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getString("comment_text"),
                        rs.getTimestamp("commented_at").toLocalDateTime()
                );
                comments.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }
}
