public class CommentController {

    public static void commentOnPost(int userId, int postId, String comment) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO comments (user_id, post_id, content) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, postId);
            stmt.setString(3, comment);
            stmt.executeUpdate();

            // Fetch post owner
            String query = "SELECT user_id FROM posts WHERE id = ?";
            PreparedStatement ownerStmt = conn.prepareStatement(query);
            ownerStmt.setInt(1, postId);
            ResultSet rs = ownerStmt.executeQuery();
            if (rs.next()) {
                int postOwnerId = rs.getInt("user_id");
                if (postOwnerId != userId) {
                    NotificationController.createNotification(postOwnerId, "comment", postId, "Someone commented on your post.");
                    WebSocketNotificationServer.sendNotification(postOwnerId, "Your post received a comment.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
