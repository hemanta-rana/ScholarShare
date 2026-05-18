package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.AdminDao;
import com.ScholarShare.entity.Flag;
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

public class AdminDaoImp implements AdminDao {


    @Override
    public int getNumberOfPendingRegistration() {

        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) AS count FROM users WHERE role='student' AND status='pending'";
           PreparedStatement preparedStatement = connection.prepareStatement(sql);
           ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                return rs.getInt("count");
            }
        }catch (SQLException e){
            System.out.println("unable to get the number of pending registration ");
            return 0;
        }finally {
            DatabaseConnection.closeConnection(connection);
        }
        return 0;
    }

    @Override
    public int getPendingSubmissions() {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) AS count FROM resources WHERE status='pending'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt("count");
            }
        }catch (SQLException e){
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return 0;
    }

    @Override
    public int getNumberOfOpenFlags() {
        String sql = "SELECT COUNT(*) AS count FROM flags WHERE status='open'";
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt("count");
            }
        }catch (SQLException e){
            System.out.println("cannot get number of open flags"+e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return 0;
    }

    @Override
    public int getTotalResources() {
        String sql = "SELECT COUNT(*) AS count FROM resources";
         Connection connection = null;
         try {
             connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();

             if (rs.next()){
                 return rs.getInt("count");
             }
         }catch (SQLException e){
             e.printStackTrace();
         } finally {
             DatabaseConnection.closeConnection(connection);
         }
        return 0;
    }

    @Override
    public int getApprovalRate() {
        String sql = """
                SELECT 
                    (COUNT(CASE WHEN status='approved' THEN 1 END) * 100) / COUNT(*) AS rate
                FROM resources
                """;
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return rs.getInt("rate");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return 0;
    }

    @Override
    public List<Resource> getRecentSubmission() {
        List<Resource> resources = new ArrayList<>();

        // JOIN with users and topics to get the submitter name and category
        String sql = "SELECT r.*, u.full_name AS submitter_name, t.topic_name " +
                     "FROM resources r " +
                     "JOIN users u ON r.user_id = u.user_id " +
                     "JOIN topics t ON r.topic_id = t.topic_id " +
                     "ORDER BY r.upload_date DESC LIMIT 5";

        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Resource resource = new Resource();
                resource.setResourceId(rs.getInt("resource_id"));
                resource.setTitle(rs.getString("title"));
                resource.setStatus(rs.getString("status"));
                resource.setUploadDate(rs.getTimestamp("upload_date"));
                resource.setResourceType(rs.getString("resource_type")); // Added resource type
                resource.setUserId(rs.getInt("user_id"));
                
                // Map the joined fields
                resource.setSubmitterName(rs.getString("submitter_name"));
                resource.setTopicName(rs.getString("topic_name"));

                resources.add(resource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return resources;
    }

    @Override
    public List<User> getPendingUserRegistration() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role='student' AND status='pending'";
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setStatus(rs.getString("status"));

                users.add(user);
            }
        }catch (SQLException e){
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return users;
    }
    @Override
    public List<Flag> getRecentFlags() {
        List<Flag> flags = new ArrayList<>();

        // JOIN with resources and users to get  titles and names
        String sql = "SELECT f.*, r.title AS resource_title, u.full_name AS flagged_by_name " +
                     "FROM flags f " +
                     "JOIN resources r ON f.resource_id = r.resource_id " +
                     "JOIN users u ON f.flagged_by = u.user_id " +
                     "WHERE f.status = 'open' " +
                     "ORDER BY f.created_at DESC LIMIT 5";

        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Flag flag = new Flag();
                flag.setFlagId(rs.getInt("flag_id"));
                flag.setReason(rs.getString("reason"));
                flag.setStatus(rs.getString("status"));
                flag.setResourceId(rs.getInt("resource_id"));
                flag.setFlaggedBy(rs.getInt("flagged_by"));

                // Map joined fields
                flag.setResourceTitle(rs.getString("resource_title"));
                flag.setFlaggedByName(rs.getString("flagged_by_name"));

                flags.add(flag);
            }
        }catch (SQLException e){
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return flags;
    }

    @Override
    public Map<String, Integer> getWeeklySubmissionCounts() {
        Map<String, Integer> counts = new HashMap<>();
        
        // Use DATE() and group by to count resources uploaded per day for the last 7 days
        String sql = "SELECT DATE(upload_date) as upload_day, COUNT(*) as daily_count " +
                     "FROM resources " +
                     "WHERE upload_date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
                     "GROUP BY DATE(upload_date) " +
                     "ORDER BY upload_day ASC";

        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String day = rs.getString("upload_day");
                int count = rs.getInt("daily_count");
                counts.put(day, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return counts;
    }

    @Override
    public List<User> getAllPendingRegistrations() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role='student' AND status='pending' ORDER BY created_at DESC";
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return users;
    }

    @Override
    public boolean updateUserStatus(int userId, String status) {
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public List<Resource> getPipelineResources() {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name AS submitter_name, t.topic_name, s.subject_name "
                + "FROM resources r "
                + "JOIN users u ON r.user_id = u.user_id "
                + "JOIN topics t ON r.topic_id = t.topic_id "
                + "JOIN subjects s ON t.subject_id = s.subject_id "
                + "WHERE r.status IN ('pending', 'under_review') "
                + "ORDER BY r.upload_date ASC";
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Resource resource = mapResourceRow(rs);
                resource.setSubmitterName(rs.getString("submitter_name"));
                resource.setTopicName(rs.getString("topic_name"));
                resource.setSubjectName(rs.getString("subject_name"));
                resources.add(resource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return resources;
    }

    @Override
    public boolean updateResourceStatus(int resourceId, String status) {
        String sql = "UPDATE resources SET status = ? WHERE resource_id = ?";
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, resourceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public List<Flag> getAllFlags() {
        return new FlagDaoImpl().getAllFlags();
    }

    @Override
    public boolean updateFlagStatus(int flagId, String status) {
        return new FlagDaoImpl().updateFlagStatus(flagId, status);
    }

    @Override
    public List<Map<String, Object>> getTopContributors(int limit) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT u.user_id, u.full_name, COUNT(*) AS approved_count "
                + "FROM resources r JOIN users u ON r.user_id = u.user_id "
                + "WHERE r.status = 'approved' GROUP BY u.user_id, u.full_name "
                + "ORDER BY approved_count DESC LIMIT ?";
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("name", rs.getString("full_name"));
                row.put("count", rs.getInt("approved_count"));
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getMostFlaggedResources(int limit) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT r.resource_id, r.title, COUNT(f.flag_id) AS flag_count "
                + "FROM flags f JOIN resources r ON f.resource_id = r.resource_id "
                + "GROUP BY r.resource_id, r.title ORDER BY flag_count DESC LIMIT ?";
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("title", rs.getString("title"));
                row.put("count", rs.getInt("flag_count"));
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return list;
    }

    private Resource mapResourceRow(ResultSet rs) throws SQLException {
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
        return resource;
    }

    /* ── Admin profile methods ── */

    @Override
    public User getAdminById(int userId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM users WHERE user_id = ? AND role = 'admin'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("status"),
                        rs.getString("profile_pic"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.out.println("AdminDaoImp: failed to get admin by id " + userId + " — " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    @Override
    public boolean updateAdminProfile(int userId, String fullName, String phone) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE users SET full_name = ?, phone = ? WHERE user_id = ? AND role = 'admin'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("AdminDaoImp: failed to update admin profile " + userId + " — " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public boolean uploadAdminProfilePicture(int userId, String relativePath) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE users SET profile_pic = ? WHERE user_id = ? AND role = 'admin'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, relativePath);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("AdminDaoImp: failed to update admin profile pic " + userId + " — " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }
}
