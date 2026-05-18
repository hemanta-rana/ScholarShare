package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.FlagDao;
import com.ScholarShare.entity.Flag;
import com.ScholarShare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlagDaoImpl implements FlagDao {

    @Override
    public boolean createFlag(Flag flag) {
        if (hasStudentAlreadyFlagged(flag.getResourceId(), flag.getFlaggedBy())) {
            return false;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO flags (resource_id, flagged_by, reason, status) VALUES (?, ?, ?, 'open')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, flag.getResourceId());
            ps.setInt(2, flag.getFlaggedBy());
            ps.setString(3, flag.getReason());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("createFlag failed " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public boolean hasStudentAlreadyFlagged(int resourceId, int studentId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM flags WHERE resource_id = ? AND flagged_by = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resourceId);
            ps.setInt(2, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("hasStudentAlreadyFlagged failed " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public List<Flag> getAllFlags() {
        List<Flag> flags = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT f.*, r.title AS resource_title, r.resource_type, t.topic_name, "
                    + "u.full_name AS flagged_by_name, ru.full_name AS resource_owner_name "
                    + "FROM flags f "
                    + "JOIN resources r ON f.resource_id = r.resource_id "
                    + "JOIN topics t ON r.topic_id = t.topic_id "
                    + "JOIN users u ON f.flagged_by = u.user_id "
                    + "JOIN users ru ON r.user_id = ru.user_id "
                    + "ORDER BY f.created_at DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                flags.add(mapFlagRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("getAllFlags failed " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return flags;
    }

    @Override
    public boolean updateFlagStatus(int flagId, String status) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE flags SET status = ? WHERE flag_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, flagId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("updateFlagStatus failed " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public Flag getById(int flagId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT f.*, r.title AS resource_title, r.resource_type, t.topic_name, "
                    + "u.full_name AS flagged_by_name, ru.full_name AS resource_owner_name "
                    + "FROM flags f "
                    + "JOIN resources r ON f.resource_id = r.resource_id "
                    + "JOIN topics t ON r.topic_id = t.topic_id "
                    + "JOIN users u ON f.flagged_by = u.user_id "
                    + "JOIN users ru ON r.user_id = ru.user_id "
                    + "WHERE f.flag_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, flagId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapFlagRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("getById flag failed " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    private Flag mapFlagRow(ResultSet rs) throws SQLException {
        Flag flag = new Flag();
        flag.setFlagId(rs.getInt("flag_id"));
        flag.setResourceId(rs.getInt("resource_id"));
        flag.setFlaggedBy(rs.getInt("flagged_by"));
        flag.setReason(rs.getString("reason"));
        flag.setStatus(rs.getString("status"));
        flag.setCreatedAt(rs.getTimestamp("created_at"));
        flag.setResourceTitle(rs.getString("resource_title"));
        flag.setFlaggedByName(rs.getString("flagged_by_name"));
        return flag;
    }
}