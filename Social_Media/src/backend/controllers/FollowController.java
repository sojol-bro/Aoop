public class FollowController {

    public static boolean follow(int followerId, int followedId) {
        String sql = "INSERT IGNORE INTO followers (follower_id, followed_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, followerId);
            stmt.setInt(2, followedId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean unfollow(int followerId, int followedId) {
        String sql = "DELETE FROM followers WHERE follower_id = ? AND followed_id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, followerId);
            stmt.setInt(2, followedId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isFollowing(int followerId, int followedId) {
        String sql = "SELECT COUNT(*) FROM followers WHERE follower_id = ? AND followed_id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, followerId);
            stmt.setInt(2, followedId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getFollowerCount(int userId) {
        String sql = "SELECT COUNT(*) FROM followers WHERE followed_id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getFollowingCount(int userId) {
        String sql = "SELECT COUNT(*) FROM followers WHERE follower_id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
