package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.ModerationLogDao;
import com.ScholarShare.entity.ModerationLog;
import com.ScholarShare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModerationLogDaoImpl implements ModerationLogDao {

    @Override
    public boolean addLog(ModerationLog log) {
        String query = "INSERT INTO moderation_logs (resource_id, admin_id, action, note) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, log.getResourceId());
            stmt.setInt(2, log.getAdminId());
            stmt.setString(3, log.getAction());
            stmt.setString(4, log.getNote());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ModerationLog> getAllLogs() {
        List<ModerationLog> logs = new ArrayList<>();
        String query = "SELECT m.*, u.full_name, r.title FROM moderation_logs m " +
                       "LEFT JOIN users u ON m.admin_id = u.user_id " +
                       "LEFT JOIN resources r ON m.resource_id = r.resource_id " +
                       "ORDER BY m.actioned_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ModerationLog log = new ModerationLog();
                log.setLogId(rs.getInt("log_id"));
                log.setResourceId(rs.getInt("resource_id"));
                log.setAdminId(rs.getInt("admin_id"));
                log.setAction(rs.getString("action"));
                log.setNote(rs.getString("note"));
                log.setActionAt(rs.getTimestamp("actioned_at"));
                
                log.setAdminName(rs.getString("full_name"));
                log.setResourceTitle(rs.getString("title"));
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}
