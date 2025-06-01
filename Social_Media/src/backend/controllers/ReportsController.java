package controllers;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportsController {

    public static void submitReport(int reporterId, Integer reportedUserId, Integer reportedPostId, String reason, String details) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO reports (reporter_id, reported_user_id, reported_post_id, reason, details) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, reporterId);
            if (reportedUserId != null) {
                stmt.setInt(2, reportedUserId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            if (reportedPostId != null) {
                stmt.setInt(3, reportedPostId);
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, reason);
            stmt.setString(5, details);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getAllReports() {
        List<String> reports = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT r.id, u1.name AS reporter, u2.name AS reportedUser, p.content AS reportedPost, r.reason, r.details, r.status, r.created_at " +
                         "FROM reports r " +
                         "LEFT JOIN users u1 ON r.reporter_id = u1.id " +
                         "LEFT JOIN users u2 ON r.reported_user_id = u2.id " +
                         "LEFT JOIN posts p ON r.reported_post_id = p.id " +
                         "ORDER BY r.created_at DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String report = String.format("Report ID: %d\nReporter: %s\nReported User: %s\nReported Post: %s\nReason: %s\nDetails: %s\nStatus: %s\nDate: %s\n---",
                        rs.getInt("id"),
                        rs.getString("reporter"),
                        rs.getString("reportedUser") == null ? "N/A" : rs.getString("reportedUser"),
                        rs.getString("reportedPost") == null ? "N/A" : rs.getString("reportedPost"),
                        rs.getString("reason"),
                        rs.getString("details"),
                        rs.getString("status"),
                        rs.getString("created_at"));
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    public static void updateReportStatus(int reportId, String newStatus) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE reports SET status = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newStatus);
            stmt.setInt(2, reportId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
