package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.UserDao;
import com.ScholarShare.entity.User;
import com.ScholarShare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// all the CRUD operation for the user
public class UserDaoImpl implements UserDao {


    @Override
    public User getUser(int userId) {
        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection(connection);
            String sql = "SELECT * FROM users WHERE userid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("user_id"));
                user.setFullName(resultSet.getString("full_name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setPhone(resultSet.getString("phone"));
                user.setRole(resultSet.getString("role"));
                user.setStatus(resultSet.getString("status"));
                user.setProfilePic(resultSet.getString("profile_pic"));
                user.setCreatedAt(resultSet.getTimestamp("created_at"));
                return user;
            }

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("sql error"+e.getMessage());
            return null;
        }finally {
           DatabaseConnection.closeConnection(connection);
        }
        return null;
    }

    @Override
    public boolean addUser(User user) {
        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection(connection);
            String sql = "INSERT INTO users(full_name, email, phone, password, role, status, profile_pic, created_at) VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getRole());
            statement.setString(6,user.getStatus());
            statement.setString(7, user.getProfilePic());
            statement.setTimestamp(8, user.getCreatedAt());
            statement.execute();
            return true;
        }catch (SQLException e){
            System.out.println("sql error"+e.getMessage());
            e.printStackTrace();
            return false;
        }finally {
            DatabaseConnection.closeConnection(connection);
        }

    }

    @Override
    public boolean updateUser(User user, int userId) {

        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection(connection);
            String sql = "UPDATE users SET full_name = ?,email = ?,phone = ?,password = ?,role = ?,status = ?,profile_pic = ? WHERE user_id = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getRole());
            statement.setString(6, user.getStatus());
            statement.setString(7, user.getProfilePic());
            statement.setInt(8, userId);
            statement.execute();
            return true;


        } catch (SQLException e) {
            System.out.println("sql error "+e.getMessage());
            e.printStackTrace();

        }finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public User getUserByEmail(String email) {
        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection(connection);
            String sql = "SELECT * FROM users WHERE email = ? AND status ='active'";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("status "),
                        rs.getString("profile_pic"),
                        rs.getTimestamp("created_at")
                );
            }
        }catch (SQLException e){
            System.out.println("sql error"+e.getMessage());
            return null;
        }finally {
            DatabaseConnection.closeConnection(connection);
        }
        return null;
    }

    @Override
    public boolean deleteUser(int userId) {
        return false;
    }

    @Override
    public List<User> getAllStudents() {
       Connection connection = null;
        ArrayList<User> users = new ArrayList<>();
       try{
           connection = DatabaseConnection.getConnection(connection);
           String sql = "SELECT * FROM users WHERE role='student' as";
           PreparedStatement statement = connection.prepareStatement(sql);
           ResultSet resultSet = statement.executeQuery();
           if(resultSet.next()){
               users.add(new User(
                       resultSet.getInt("user_id"),
                       resultSet.getString("full_name"),
                       resultSet.getString("email"),
                       resultSet.getString("phone"),
                       resultSet.getString("password"),
                       resultSet.getString("role"),
                       resultSet.getString("status"),
                       resultSet.getString("profile_pic"),
                       resultSet.getTimestamp("created_at")
               ));
           }
       }catch (SQLException e){
           System.out.println("sql error"+e.getMessage());
           e.printStackTrace();
           return users;
       }finally {
           DatabaseConnection.closeConnection(connection);
       }
       return users;
    }
}
