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
    public List<Collection> getStudentCollections(int userId) {
        ArrayList<Collection>  collections = new ArrayList<>();
        Connection conn = null;
        try{
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM collections WHERE user_id = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
               Collection collection = new Collection();
              collection.setCollectionId(rs.getInt("collection_id"));
              collection.setCollectionName(rs.getString("collection_name"));
              collection.setCreatedAt(rs.getTimestamp("created_at"));
              collections.add(collection);
            }

        }catch (SQLException e){
            System.out.println("cannot get student collections by student id "+userId+e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(conn);
        }
        return collections;
    }

    @Override
    public boolean agreeToIntegrityPledge(int userId) {

        return false;
    }

    @Override
    public boolean uploadResource(Resource resource) {
        return false;
    }

    @Override
    public boolean deleteResource(int resourceId) {
        return false;
    }

    @Override
    public int getContributorReputationScore(int userId) {
        return 0;
    }

    @Override
    public boolean createCollection(int userId, String collectionName) {
        return false;
    }

    @Override
    public boolean addResourceToCollection(int collectionId, int resourceId) {
        return false;
    }

    @Override
    public boolean deleteCollection(int collectionId) {
        return false;
    }

    @Override
    public boolean deleteResourceFromCollection(int collectionId, int resourceId) {
        return false;
    }

    @Override
    public boolean flagResourceForPlagiarism(int resourceId, int userId, String reason) {
        return false;
    }

    @Override
    public boolean rateResource(int resourceId, int userId, int score) {
        return false;
    }
}
