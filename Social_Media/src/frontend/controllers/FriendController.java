package controllers;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendController {

    // Send friend request
    public static boolean sendFriendRequest(int senderId, int receiverId) {
        try (Connection conn = DBConnection.getConnection()) {
            // Check if request already exists or they are already friends
            String checkSql = "SELECT * FROM friend_requests WHERE sender_id = ? AND receiver_id = ? AND status = 'pending'";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, senderId);
            checkStmt.setInt(2, receiverId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                return false; // Already pending
            }
            // Insert friend request
            String sql = "INSERT INTO friend_requests (sender_id, receiver_id) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Accept friend request
    public static boolean acceptFriendRequest(int requestId) {
        try (Connection conn = DBConnection.getConnection()) {
            // Get request info
            String getReqSql = "SELECT sender_id, receiver_id FROM friend_requests WHERE id = ? AND status = 'pending'";
            PreparedStatement getReqStmt = conn.prepareStatement(getReqSql);
            getReqStmt.setInt(1, requestId);
            ResultSet rs = getReqStmt.executeQuery();
            if (!rs.next()) return false;

            int senderId = rs.getInt("sender_id");
            int receiverId = rs.getInt("receiver_id");

            // Update request status
            String updateSql = "UPDATE friend_requests SET status = 'accepted' WHERE id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setInt(1, requestId);
            updateStmt.executeUpdate();

            // Add to friends table (store smaller ID first)
            int user1 = Math.min(senderId, receiverId);
            int user2 = Math.max(senderId, receiverId);
            String friendSql = "INSERT INTO friends (user1_id, user2_id) VALUES (?, ?)";
            PreparedStatement friendStmt = conn.prepareStatement(friendSql);
            friendStmt.setInt(1, user1);
            friendStmt.setInt(2, user2);
            friendStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Decline friend request
    public static boolean declineFriendRequest(int requestId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE friend_requests SET status = 'declined' WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, requestId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // List pending requests for a user
    public static List<String> getPendingRequests(int userId) {
        List<String> requests = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT fr.id, u.username FROM friend_requests fr JOIN users u ON fr.sender_id = u.id WHERE fr.receiver_id = ? AND fr.status = 'pending'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                requests.add("Request ID: " + rs.getInt("id") + " from: " + rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    // List friends for a user
    public static List<String> getFriends(int userId) {
        List<String> friends = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT u.username FROM users u JOIN friends f ON (u.id = f.user1_id OR u.id = f.user2_id) WHERE (f.user1_id = ? OR f.user2_id = ?) AND u.id != ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            stmt.setInt(3, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                friends.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }
}
