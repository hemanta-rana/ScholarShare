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
 * Data access for student dashboard and student-specific features.
 *
 * @author Hemanta Rana
 */
public class StudentDaoImpl implements StudentDao {

    @Override
    public User getUserById(int userId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT user_id, full_name, email, phone, password, role, status, profile_pic, created_at "
                    + "FROM users WHERE user_id = ? AND role = 'student' AND status = 'active'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            System.out.println("sql error " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    @Override
    public User getStudentByEmail(String email) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT user_id, full_name, email, phone, password, role, status, profile_pic, created_at "
                    + "FROM users WHERE email = ? AND role = 'student'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            System.out.println("cannot get student by email " + email + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    @Override
    public boolean agreeToIntegrityPledge(int userId) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO integrity_pledges (user_id, agreed, agreed_at) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setBoolean(2, true);
            ps.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("failed to add integrity pledge by student id " + userId + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public int getContributorReputationScore(int userId) {
        String sql = """
                SELECT LEAST(100, GREATEST(0,
                    (COALESCE(SUM(CASE WHEN status = 'approved' THEN 1 ELSE 0 END), 0) * 6)
                  + (COALESCE(SUM(CASE WHEN status = 'under_review' THEN 1 ELSE 0 END), 0) * 2)
                  - (COALESCE(SUM(CASE WHEN status = 'rejected' THEN 1 ELSE 0 END), 0) * 3)
                )) AS score
                FROM resources
                WHERE user_id = ?
                """;
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("score");
            }
        } catch (SQLException e) {
            System.out.println("failed to get reputation score for user id " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }

    @Override
    public boolean flagResourceForPlagiarism(int resourceId, int userId, String reason) {
        String sql = "INSERT INTO flags (resource_id, flagged_by, reason, status) VALUES (?, ?, ?, 'open')";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resourceId);
            ps.setInt(2, userId);
            ps.setString(3, reason);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("failed to flag resource " + resourceId + " by user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public boolean updateProfilePicture(int userId, String profilePicPath) {
        String sql = "UPDATE users SET profile_pic = ? WHERE user_id = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, profilePicPath);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("failed to update profile picture for user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public Map<String, Integer> countUploadsByStatus(int userId) {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("total", 0);
        counts.put("pending", 0);
        counts.put("underReview", 0);
        counts.put("approved", 0);
        counts.put("rejected", 0);

        String sql = """
                SELECT status, COUNT(*) AS cnt
                FROM resources
                WHERE user_id = ?
                GROUP BY status
                """;
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            int total = 0;
            while (rs.next()) {
                String status = rs.getString("status");
                int cnt = rs.getInt("cnt");
                total += cnt;
                switch (status) {
                    case "pending" -> counts.put("pending", cnt);
                    case "under_review" -> counts.put("underReview", cnt);
                    case "approved" -> counts.put("approved", cnt);
                    case "rejected" -> counts.put("rejected", cnt);
                    default -> { }
                }
            }
            counts.put("total", total);
        } catch (SQLException e) {
            System.out.println("failed to count uploads for user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return counts;
    }

    @Override
    public int countSavedResources(int userId) {
        String sql = """
                SELECT COUNT(DISTINCT ci.resource_id) AS cnt
                FROM collection_items ci
                JOIN collections c ON ci.collection_id = c.collection_id
                WHERE c.user_id = ?
                """;
        return querySingleInt(sql, userId);
    }

    @Override
    public int countApprovedUploadsSince(int userId, int daysBack) {
        String sql = """
                SELECT COUNT(*) AS cnt
                FROM resources
                WHERE user_id = ?
                  AND status = 'approved'
                  AND upload_date >= DATE_SUB(CURDATE(), INTERVAL ? DAY)
                """;
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, daysBack);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            System.out.println("failed to count recent approved uploads " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }

    @Override
    public List<Resource> getRecentSubmissionsByUser(int userId, int limit) {
        String sql = """
                SELECT r.*, t.topic_name, s.subject_name
                FROM resources r
                JOIN topics t ON r.topic_id = t.topic_id
                JOIN subjects s ON t.subject_id = s.subject_id
                WHERE r.user_id = ?
                ORDER BY r.upload_date DESC
                LIMIT ?
                """;
        return queryResourceList(sql, userId, limit);
    }

    @Override
    public List<Resource> getRecentlyAddedApprovedResources(int excludeUserId, int limit) {
        String sql = """
                SELECT r.*, t.topic_name, s.subject_name, u.full_name AS submitter_name
                FROM resources r
                JOIN topics t ON r.topic_id = t.topic_id
                JOIN subjects s ON t.subject_id = s.subject_id
                JOIN users u ON r.user_id = u.user_id
                WHERE r.status = 'approved'
                  AND r.user_id <> ?
                ORDER BY r.upload_date DESC
                LIMIT ?
                """;
        Connection conn = null;
        List<Resource> resources = new ArrayList<>();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, excludeUserId);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resources.add(mapResource(rs, true));
            }
        } catch (SQLException e) {
            System.out.println("failed to load recently added resources " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return resources;
    }

    @Override
    public List<Resource> searchStudentSubmissions(int userId, String query, int limit) {
        String sql = """
                SELECT r.*, t.topic_name, s.subject_name
                FROM resources r
                JOIN topics t ON r.topic_id = t.topic_id
                JOIN subjects s ON t.subject_id = s.subject_id
                WHERE r.user_id = ?
                  AND (r.title LIKE ? OR t.topic_name LIKE ? OR s.subject_name LIKE ?)
                ORDER BY r.upload_date DESC
                LIMIT ?
                """;
        String pattern = "%" + query.trim() + "%";
        Connection conn = null;
        List<Resource> resources = new ArrayList<>();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            ps.setInt(5, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resources.add(mapResource(rs, false));
            }
        } catch (SQLException e) {
            System.out.println("failed to search student submissions " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return resources;
    }

    @Override
    public double getAccuracyRate(int userId) {
        String sql = """
                SELECT
                    COALESCE(SUM(CASE WHEN status = 'approved' THEN 1 ELSE 0 END), 0) AS approved_cnt,
                    COALESCE(SUM(CASE WHEN status IN ('approved', 'rejected') THEN 1 ELSE 0 END), 0) AS decided_cnt
                FROM resources
                WHERE user_id = ?
                """;
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int decided = rs.getInt("decided_cnt");
                if (decided == 0) {
                    return 0;
                }
                int approved = rs.getInt("approved_cnt");
                return Math.round((approved * 100.0) / decided);
            }
        } catch (SQLException e) {
            System.out.println("failed to get accuracy rate " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }

    @Override
    public double getPeerHelpfulness(int userId) {
        String sql = """
                SELECT COALESCE(AVG(rt.score), 0) AS avg_rating
                FROM ratings rt
                JOIN resources r ON rt.resource_id = r.resource_id
                WHERE r.user_id = ?
                  AND r.status = 'approved'
                """;
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double avg = rs.getDouble("avg_rating");
                if (avg <= 0) {
                    return 0;
                }
                return Math.round((avg / 5.0) * 100);
            }
        } catch (SQLException e) {
            System.out.println("failed to get peer helpfulness " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }

    @Override
    public int getContributorPercentileRank(int userId) {
        int myScore = getContributorReputationScore(userId);
        String sql = """
                SELECT COUNT(*) AS better_cnt
                FROM (
                    SELECT u.user_id,
                           LEAST(100, GREATEST(0,
                               (COALESCE(SUM(CASE WHEN r.status = 'approved' THEN 1 ELSE 0 END), 0) * 6)
                             + (COALESCE(SUM(CASE WHEN r.status = 'under_review' THEN 1 ELSE 0 END), 0) * 2)
                             - (COALESCE(SUM(CASE WHEN r.status = 'rejected' THEN 1 ELSE 0 END), 0) * 3)
                           )) AS score
                    FROM users u
                    LEFT JOIN resources r ON r.user_id = u.user_id
                    WHERE u.role = 'student' AND u.status = 'active'
                    GROUP BY u.user_id
                ) ranked
                WHERE score > ?
                """;
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, myScore);
            ResultSet rs = ps.executeQuery();
            int better = 0;
            if (rs.next()) {
                better = rs.getInt("better_cnt");
            }

            int totalStudents = countActiveStudents();
            if (totalStudents <= 1) {
                return 5;
            }
            int percentile = (int) Math.round((1.0 - ((double) better / totalStudents)) * 100);
            return Math.max(1, Math.min(99, percentile));
        } catch (SQLException e) {
            System.out.println("failed to get percentile rank " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 50;
    }

    private int countActiveStudents() {
        return querySingleInt("SELECT COUNT(*) AS cnt FROM users WHERE role = 'student' AND status = 'active'", null);
    }

    private int querySingleInt(String sql, Integer userId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            if (userId != null) {
                ps.setInt(1, userId);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            System.out.println("querySingleInt error " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return 0;
    }

    private List<Resource> queryResourceList(String sql, int userId, int limit) {
        Connection conn = null;
        List<Resource> resources = new ArrayList<>();
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resources.add(mapResource(rs, false));
            }
        } catch (SQLException e) {
            System.out.println("queryResourceList error " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return resources;
    }

    private User mapUser(ResultSet rs) throws SQLException {
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

    private Resource mapResource(ResultSet rs, boolean includeSubmitter) throws SQLException {
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
        resource.setTopicName(rs.getString("topic_name"));
        try {
            resource.setSubjectName(rs.getString("subject_name"));
        } catch (SQLException ignored) {
            // subject_name not in every query
        }
        if (includeSubmitter) {
            try {
                resource.setSubmitterName(rs.getString("submitter_name"));
            } catch (SQLException ignored) { }
        }
        return resource;
    }
}
