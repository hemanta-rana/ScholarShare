package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.FlagDao;
import com.ScholarShare.entity.Flag;
import com.ScholarShare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}