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
}
