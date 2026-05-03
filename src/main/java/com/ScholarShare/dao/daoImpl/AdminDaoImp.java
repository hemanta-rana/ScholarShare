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

        }
    }

    @Override
    public int getNumberOfOpenFlags() {
        return 0;
    }

    @Override
    public int getTotalResources() {
        return 0;
    }

    @Override
    public int getApprovalRate() {
        return 0;
    }

    @Override
    public List<Resource> getRecentSubmission() {
        return List.of();
    }

    @Override
    public List<User> getPendingUserRegistration() {
        return List.of();
    }

    @Override
    public List<Flag> getRecentFlags() {
        return List.of();
    }
}
