package backend.controllers;

import backend.configs.DBConnection;
import backend.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserSearchController {

    public static List<User> searchUsers(String keyword) {
        List<User> results = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE username LIKE ? OR email LIKE ?";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            String search = "%" + keyword + "%";
            stmt.setString(1, search);
            stmt.setString(2, search);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),

                    rs.getString("bio"),
                    rs.getString("profile_pic"),
                    rs.getString("location")
                );
                results.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
}
