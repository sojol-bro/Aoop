public class ProfileController {

    public static User getProfile(int userId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("bio"),
                    rs.getString("profile_pic"),
                    rs.getString("location")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateProfile(int userId, String bio, String location, String profilePicPath) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE users SET bio = ?, location = ?, profile_pic = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, bio);
            stmt.setString(2, location);
            stmt.setString(3, profilePicPath);
            stmt.setInt(4, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
