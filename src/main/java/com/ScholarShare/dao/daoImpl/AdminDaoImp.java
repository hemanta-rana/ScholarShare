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
import java.util.List;

public class AdminDaoImp implements AdminDao {


    @Override
    public int getNumberOfPendingRegistration() {

        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) AS count FROM USER  WHERE role='student' AND status='pending'";
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
            String sql = "SELECT COUNT(*) AS count FROM resources WHERE  status='pending'";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt("count");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getNumberOfOpenFlags() {
        String sql = "SELECT COUNT(*) AS count FROM flags WHERE status='open'";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt("count");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getTotalResources() {
        String sql = "SELECT COUNT(*) AS count FROM resources";
         try {
             Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();

             if (rs.next()){
                 return rs.getInt("count");
             }
         }catch (SQLException e){
             e.printStackTrace();
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
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return rs.getInt("rate");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Resource> getRecentSubmission() {
        List<Resource> resources = new ArrayList<>();

        String sql = "SELECT * FROM resources ORDER BY upload_date DESC LIMIT 5";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Resource resource = new Resource();
                resource.setResourceId(rs.getInt("resource_id"));
                resource.setTitle(rs.getString("title"));
                resource.setStatus(rs.getString("status"));
                resource.setUploadDate(rs.getTimestamp("upload_date"));

                resources.add(resource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;
    }

    @Override
    public List<User> getPendingUserRegistration() {
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM user WHERE role='student' AND status='pending'";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFullName(rs.getString("Full_name"));
                user.setEmail(rs.getString("email"));
                user.setStatus(rs.getString("status"));

                users.add(user);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }
    public List<Flag> getRecentFlags() {
        List<Flag> flags = new ArrayList<>();

        String sql = "SELECT * FROM flags ORDER BY created_at DESC LIMIT 5";

        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Flag flag = new Flag();
                flag.setFlagId(rs.getInt("flag_id"));
                flag.setReason(rs.getString("reason"));
                flag.setStatus(rs.getString("status"));

                flags.add(flag);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return flags;
    }
}
