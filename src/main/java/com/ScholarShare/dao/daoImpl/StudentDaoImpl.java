package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.StudentDao;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.User;
import com.ScholarShare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            String sql = "SELECT * FROM users  WHERE user_id = ? AND role='student' AND status='active' ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
               return new User(
                        rs.getInt("user_id"),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("password"),
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
            String sql = "SELECT "
                    + "COALESCE(SUM(CASE WHEN status = 'approved' THEN 1 ELSE 0 END), 0) AS approved_cnt, "
                    + "COALESCE(SUM(CASE WHEN status = 'rejected' THEN 1 ELSE 0 END), 0) AS rejected_cnt "
                    + "FROM resources WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int approved = rs.getInt("approved_cnt");
                int rejected = rs.getInt("rejected_cnt");
                int score = approved * 20 - rejected * 15;
                return Math.max(0, Math.min(100, score));
            }
        } catch (SQLException e) {
            System.out.println("failed to get reputation score for user id " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }

    @Override
    public String getContributorStatus(int userId) {
        int score = getContributorReputationScore(userId);

        if (score >= THRESHOLD_PLATINUM) return "PLATINUM";
        if (score >= THRESHOLD_GOLD)     return "GOLD";
        if (score >= THRESHOLD_SILVER)   return "SILVER";
        return "BRONZE";
    }


    @Override
    public boolean updateProfile(int userId, String fullName, String phone) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE users SET full_name = ?, phone = ? WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("failed to update profile for user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    private Resource mapSubmissionResource(ResultSet rs) throws SQLException {
        Resource resource = new Resource();
        resource.setResourceId(rs.getInt("resource_id"));
        resource.setUserId(rs.getInt("user_id"));
        resource.setTopicId(rs.getInt("topic_id"));
        resource.setTitle(rs.getString("title"));
        resource.setDescription(rs.getString("description"));
        resource.setFilePath(rs.getString("file_path"));
        resource.setResourceType(rs.getString("resource_type"));
        resource.setStatus(rs.getString("status"));
        resource.setSelfDeclaration(rs.getBoolean("self_declaration"));
        resource.setUploadDate(rs.getTimestamp("upload_date"));
        String subjectName = rs.getString("subject_name");
        if (subjectName != null) {
            resource.setSubjectName(subjectName);
        }
        String topicName = rs.getString("topic_name");
        if (topicName != null) {
            resource.setTopicName(topicName);
        }
        String submitterName = rs.getString("submitter_name");
        if (submitterName != null) {
            resource.setSubmitterName(submitterName);
        }
        return resource;
    }

    @Override
    public Map<String, Integer> countUploadsByStatus(int userId) {
        Map<String, Integer> map = new HashMap<>();
        map.put("approved", 0);
        map.put("pending", 0);
        map.put("underReview", 0);
        map.put("rejected", 0);
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT status, COUNT(*) AS cnt FROM resources WHERE user_id = ? GROUP BY status";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String status = rs.getString("status");
                int c = rs.getInt("cnt");
                if ("under_review".equals(status)) {
                    map.put("underReview", c);
                } else if ("approved".equals(status)) {
                    map.put("approved", c);
                } else if ("pending".equals(status)) {
                    map.put("pending", c);
                } else if ("rejected".equals(status)) {
                    map.put("rejected", c);
                }
            }
        } catch (SQLException e) {
            System.out.println("failed to count uploads by status for user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return map;
    }

    @Override
    public int countApprovedUploadsSince(int userId, int daysBack) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) AS c FROM resources WHERE user_id = ? AND status = 'approved' "
                    + "AND upload_date >= DATE_SUB(NOW(), INTERVAL ? DAY)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, daysBack);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("c");
            }
        } catch (SQLException e) {
            System.out.println("failed to count approved uploads since for user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }

    @Override
    public int countSavedResources(int userId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) AS c FROM collection_items ci "
                    + "JOIN collections c ON ci.collection_id = c.collection_id WHERE c.user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("c");
            }
        } catch (SQLException e) {
            System.out.println("failed to count saved resources for user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }

    @Override
    public double getAccuracyRate(int userId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT "
                    + "COALESCE(SUM(CASE WHEN status = 'approved' THEN 1 ELSE 0 END), 0) AS appr, "
                    + "COALESCE(SUM(CASE WHEN status = 'rejected' THEN 1 ELSE 0 END), 0) AS rej "
                    + "FROM resources WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int appr = rs.getInt("appr");
                int rej = rs.getInt("rej");
                int denom = appr + rej;
                if (denom == 0) {
                    return 0.0;
                }
                return 100.0 * appr / denom;
            }
        } catch (SQLException e) {
            System.out.println("failed to get accuracy rate for user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0.0;
    }

    @Override
    public double getPeerHelpfulness(int userId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT COALESCE(AVG(rt.score), 0) AS avg_score FROM ratings rt "
                    + "INNER JOIN resources r ON rt.resource_id = r.resource_id WHERE r.user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avg_score");
            }
        } catch (SQLException e) {
            System.out.println("failed to get peer helpfulness for user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0.0;
    }

    @Override
    public int getContributorPercentileRank(int userId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String mySql = "SELECT COUNT(*) AS myc FROM resources WHERE user_id = ? AND status = 'approved'";
            PreparedStatement ps1 = conn.prepareStatement(mySql);
            ps1.setInt(1, userId);
            ResultSet rs1 = ps1.executeQuery();
            int myCount = 0;
            if (rs1.next()) {
                myCount = rs1.getInt("myc");
            }
            rs1.close();
            ps1.close();
            if (myCount == 0) {
                return 0;
            }

            String totalSql = "SELECT COUNT(DISTINCT user_id) AS t FROM resources WHERE status = 'approved'";
            PreparedStatement ps2 = conn.prepareStatement(totalSql);
            ResultSet rs2 = ps2.executeQuery();
            int total = 0;
            if (rs2.next()) {
                total = rs2.getInt("t");
            }
            rs2.close();
            ps2.close();
            if (total <= 1) {
                return 85;
            }

            String belowSql = "SELECT COUNT(*) AS b FROM ("
                    + "SELECT user_id FROM resources WHERE status = 'approved' GROUP BY user_id "
                    + "HAVING COUNT(*) < ?) x";
            PreparedStatement ps3 = conn.prepareStatement(belowSql);
            ps3.setInt(1, myCount);
            ResultSet rs3 = ps3.executeQuery();
            int below = 0;
            if (rs3.next()) {
                below = rs3.getInt("b");
            }
            rs3.close();
            ps3.close();

            return (int) Math.min(99, Math.round(100.0 * below / (double) Math.max(1, total - 1)));
        } catch (SQLException e) {
            System.out.println("failed to get contributor percentile for user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }

    @Override
    public List<Resource> searchStudentSubmissions(int userId, String query, int limit) {
        List<Resource> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT r.*, s.subject_name, t.topic_name, u.full_name AS submitter_name "
                    + "FROM resources r "
                    + "LEFT JOIN topics t ON r.topic_id = t.topic_id "
                    + "LEFT JOIN subjects s ON t.subject_id = s.subject_id "
                    + "LEFT JOIN users u ON r.user_id = u.user_id "
                    + "WHERE r.user_id = ? AND (r.title LIKE ? OR r.description LIKE ?) "
                    + "ORDER BY r.upload_date DESC LIMIT ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            String like = "%" + query + "%";
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setInt(4, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapSubmissionResource(rs));
            }
        } catch (SQLException e) {
            System.out.println("failed to search submissions for user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return list;
    }

    @Override
    public List<Resource> getRecentSubmissionsByUser(int userId, int limit) {
        List<Resource> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT r.*, s.subject_name, t.topic_name, u.full_name AS submitter_name "
                    + "FROM resources r "
                    + "LEFT JOIN topics t ON r.topic_id = t.topic_id "
                    + "LEFT JOIN subjects s ON t.subject_id = s.subject_id "
                    + "LEFT JOIN users u ON r.user_id = u.user_id "
                    + "WHERE r.user_id = ? "
                    + "ORDER BY r.upload_date DESC LIMIT ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapSubmissionResource(rs));
            }
        } catch (SQLException e) {
            System.out.println("failed to get recent submissions for user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return list;
    }

    @Override
    public List<Resource> getRecentlyAddedApprovedResources(int userId, int limit) {
        List<Resource> list = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT r.*, s.subject_name, t.topic_name, u.full_name AS submitter_name "
                    + "FROM resources r "
                    + "JOIN topics t ON r.topic_id = t.topic_id "
                    + "JOIN subjects s ON t.subject_id = s.subject_id "
                    + "JOIN users u ON r.user_id = u.user_id "
                    + "WHERE r.status = 'approved' AND r.user_id <> ? "
                    + "ORDER BY r.upload_date DESC LIMIT ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapSubmissionResource(rs));
            }
        } catch (SQLException e) {
            System.out.println("failed to get recently added resources for user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return list;
    }

    @Override
    public boolean flagResourceForPlagiarism(int resourceId, int userId, String reason) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO flags (resource_id, flagged_by, reason, status) VALUES (?, ?, ?, 'open')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resourceId);
            ps.setInt(2, userId);
            ps.setString(3, reason);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("failed to flag resource " + resourceId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public boolean uploadProfilePicture(int userId, String relativePath) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE users SET profile_pic = ? WHERE user_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, relativePath);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("failed to update profile picture for user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }
}
