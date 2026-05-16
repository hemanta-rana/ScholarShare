package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.RatingDao;
import com.ScholarShare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RatingDaoImpl implements RatingDao {
    @Override
    public boolean rateResource(int resourceId, int userId, int score) {
        Connection conn = null;
        String sql = "INSERT INTO ratings (resource_id, user_id, score) VALUES (?, ?, ?)";
        try {
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resourceId);
            ps.setInt(2, userId);
            ps.setInt(3, score);
            int rows = ps.executeUpdate();
            return rows > 0;
        }catch (SQLException e){
            System.out.println("Failed to save rating"+e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public double averageRating(int resourceId) {
       Connection conn = null;
       String sql = "SELECT AVG(score) AS average_score FROM ratings WHERE resource_id = ?";
       try {
           conn = DatabaseConnection.getConnection();
           PreparedStatement ps = conn.prepareStatement(sql);
           ps.setInt(1, resourceId);
           ResultSet rs = ps.executeQuery();
           if (rs.next()) {
               return rs.getDouble("average_score");
           }

       }catch (SQLException e){
           System.out.println("Failed to get average rating"+e.getMessage());
       } finally {
           DatabaseConnection.closeConnection(conn);
       }
       return 0.0;
    }

    @Override
    public boolean hasUserRated(int resourceId, int userId) {
        Connection  conn = null;
        String sql = "SELECT * FROM ratings WHERE resource_id = ? AND user_id = ?";
        try{
            conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,resourceId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }

        }catch (SQLException e){
            System.out.println("Failed to get users rating "+e.getMessage());
        }finally {
                DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

}
