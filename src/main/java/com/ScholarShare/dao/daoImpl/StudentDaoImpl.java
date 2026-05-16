package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.StudentDao;
import com.ScholarShare.entity.Collection;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.User;
import com.ScholarShare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents Student Features and utils.
 *
 * @author Hemanta Rana
 *
 */
public class StudentDaoImpl  implements StudentDao {

    private static final int THRESHOLD_SILVER   =  50;
    private static final int THRESHOLD_GOLD     = 150;
    private static final int THRESHOLD_PLATINUM = 300;
    private static final int POINTS_UPLOAD =50 ;
    private static final int POINTS_DOWNLOAD=150;
    private static final int POINTS_LIKE=300;

    /**
     * find user by its user id
     * @param userId must be integer non negative
     *
     */
    @Override
    public User getUserById(int userId) {
        Connection conn = null;
        try{
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM users  WHERE id = ? AND role='student' AND status='active' ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
               return new User(
                        rs.getInt("user_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("phone"),
                rs.getString("role"),
                rs.getString("status"),
                rs.getString("profile_pic"),
                rs.getTimestamp("created_at"));
            }

        } catch (SQLException e) {
            System.out.println("sql error "+e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    /**
     * Student by emiail
     *
     * @param email
     * @return
     */
    @Override
    public User getStudentByEmail(String email) {
       Connection conn = null;
       try {
           conn = DatabaseConnection.getConnection();
           String sql = "SELECT * FROM users WHERE email = ? ";
           PreparedStatement ps = conn.prepareStatement(sql);
           ps.setString(1, email);
           ResultSet rs = ps.executeQuery();
           if(rs.next()){
               return new User(rs.getInt("user_id"),
                       rs.getString("full_name"),
                       rs.getString("email"),
                       rs.getString("phone"),
                       rs.getString("role"),
                       rs.getString("status"),
                       rs.getString("profile_pic"),
                       rs.getTimestamp("created_at")
               );
           }

       }catch (SQLException e){
           System.out.println("cannot get student by student email "+email+e.getMessage());
       }finally {
           DatabaseConnection.closeConnection(conn);
       }
       return null;
    }


    @Override
    public boolean agreeToIntegrityPledge(int userId) {

        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO integrity_pledges (user_id, agreed, agreed_at) VALUES (?, ?, ?) ";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setBoolean(2, true);
            ps.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
            ps.execute();
            return  true;

        }catch(SQLException e){
            System.out.println("failed to add integrity pledge by student id "+userId+e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(connection);
        }

        return false;
    }


    @Override
    public int getContributorReputationScore(int userId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT COALESCE(SUM(points), 0) AS total_score "
                    + "FROM reputation_events WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_score");
            }
        } catch (SQLException e) {
            System.out.println("failed to get reputation score for user id " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }

    public String getContributorStatus(int userId) {
        int score = getContributorReputationScore(userId);

        if (score >= THRESHOLD_PLATINUM) return "PLATINUM";
        if (score >= THRESHOLD_GOLD)     return "GOLD";
        if (score >= THRESHOLD_SILVER)   return "SILVER";
        return "BRONZE";
    }
    public boolean addReputationEventOnUpload(int userId) {
        return insertReputationEvent(userId, "UPLOAD", POINTS_UPLOAD);
    }
    public boolean addReputationEventOnDownload(int contributorUserId) {
        return insertReputationEvent(contributorUserId, "DOWNLOAD", POINTS_DOWNLOAD);
    }
    public boolean addReputationEventOnLike(int contributorUserId) {
        return insertReputationEvent(contributorUserId, "LIKE", POINTS_LIKE);
    }
    private boolean insertReputationEvent(int userId, String eventType, int points) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO reputation_events (user_id, event_type, points) "
                    + "VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, eventType);
            ps.setInt(3, points);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("failed to insert reputation event " + eventType
                    + " for user id " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }
    @Override
    public boolean flagResourceForPlagiarism(int resourceId, int userId, String reason) {
        return false;
    }

    @Override
    public boolean uploadProfilePicture(int resourceId, int userId) {
        return false;
    }
}
