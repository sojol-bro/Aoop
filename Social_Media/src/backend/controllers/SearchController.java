package controllers;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchController {

    // Search Users by name or username (case-insensitive)
    public static List<String> searchUsers(String keyword) {
        List<String> users = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, username, name FROM users WHERE username LIKE ? OR name LIKE ? LIMIT 20";
            PreparedStatement stmt = conn.prepareStatement(sql);
            String likeKeyword = "%" + keyword + "%";
            stmt.setString(1, likeKeyword);
            stmt.setString(2, likeKeyword);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(rs.getString("username") + " (" + rs.getString("name") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Search Posts by keywords in content or hashtags (hashtags stored as #tag in content)
    public static List<String> searchPosts(String keyword) {
        List<String> posts = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, content FROM posts WHERE content LIKE ? LIMIT 20";
            PreparedStatement stmt = conn.prepareStatement(sql);
            String likeKeyword = "%" + keyword + "%";
            stmt.setString(1, likeKeyword);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String snippet = rs.getString("content");
                if (snippet.length() > 100) snippet = snippet.substring(0, 100) + "...";
                posts.add("Post ID " + rs.getInt("id") + ": " + snippet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
}
