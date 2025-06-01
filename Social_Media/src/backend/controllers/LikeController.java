public class LikeController {

    public static void likePost(int userId, int postId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO likes (user_id, post_id) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, postId);
            stmt.executeUpdate();

            // Fetch post owner
            String query = "SELECT user_id FROM posts WHERE id = ?";
            PreparedStatement ownerStmt = conn.prepareStatement(query);
            ownerStmt.setInt(1, postId);
            ResultSet rs = ownerStmt.executeQuery();
            if (rs.next()) {
                int postOwnerId = rs.getInt("user_id");
                if (postOwnerId != userId) {
                    NotificationController.createNotification(postOwnerId, "like", postId, "Someone liked your post.");
                    WebSocketNotificationServer.sendNotification(postOwnerId, "Your post was liked.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
