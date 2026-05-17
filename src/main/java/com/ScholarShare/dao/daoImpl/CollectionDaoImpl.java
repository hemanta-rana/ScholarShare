package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.CollectionDao;
import com.ScholarShare.entity.Collection;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CollectionDaoImpl implements CollectionDao {

    @Override
    public boolean createCollection(int userId, String collectionName) {
        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO collections (user_id, collection_name) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, collectionName);
            ps.execute();
            return true;
        }catch (SQLException e){
            System.out.println("cannot create collections by student id "+userId+e.getMessage());

        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean deleteCollection(int collectionId) {
        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM collections WHERE collection_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, collectionId);
            ps.execute();
            return true;

        }catch (SQLException e){
            System.out.println("cannot delete collections by collection id "+collectionId+e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean addResourceToCollection(int userId, int resourceId) {
        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO collections (user_id, resource_id) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, resourceId);
            ps.execute();
            return true;

        }catch (SQLException e){
            System.out.println("failed to add resource to collection" + userId+e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean deleteResourceFromCollection(int collectionId, int resourceId) {
        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM collections WHERE collection_id = ? AND resource_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, collectionId);
            ps.setInt(2, resourceId);
            ps.execute();
            return true;

        }catch (SQLException e){
            System.out.println("failed to delete resource from collection id "+collectionId+e.getMessage());
        }finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }
    @Override
    public List<Collection> getStudentCollections(int userId) {
        ArrayList<Collection> collections = new ArrayList<>();
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
    public List<Collection> getByUser(int userId) {
        List<Collection> collections = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM collections WHERE user_id = ? ORDER BY created_at DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Collection collection = new Collection();
                collection.setCollectionId(rs.getInt("collection_id"));
                collection.setUserId(rs.getInt("user_id"));
                collection.setCollectionName(rs.getString("collection_name"));
                collection.setCreatedAt(rs.getTimestamp("created_at"));

                // Load resources belonging to this collection
                List<Resource> resources = new ArrayList<>();
                String resourceSql = "SELECT r.* FROM resources r " +
                        "JOIN collection_items ci ON r.resource_id = ci.resource_id " +
                        "WHERE ci.collection_id = ?";
                PreparedStatement resourcePs = connection.prepareStatement(resourceSql);
                resourcePs.setInt(1, collection.getCollectionId());
                ResultSet resourceRs = resourcePs.executeQuery();
                while (resourceRs.next()) {
                    Resource resource = new Resource();
                    resource.setResourceId(resourceRs.getInt("resource_id"));
                    resource.setUserId(resourceRs.getInt("user_id"));
                    resource.setTopicId(resourceRs.getInt("topic_id"));
                    resource.setTitle(resourceRs.getString("title"));
                    resource.setDescription(resourceRs.getString("description"));
                    resource.setFilePath(resourceRs.getString("file_path"));
                    resource.setResourceType(resourceRs.getString("resource_type"));
                    resource.setStatus(resourceRs.getString("status"));
                    resource.setSelfDeclaration(resourceRs.getBoolean("self_declaration"));
                    resource.setUploadDate(resourceRs.getTimestamp("upload_date"));
                    resources.add(resource);
                }
                collection.setResources(resources);
                collections.add(collection);
            }
        } catch (SQLException e) {
            System.out.println("Cannot get collections for user id " + userId + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return collections;
    }

    @Override
    public boolean addResource(int collectionId, int resourceId) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO collection_items (collection_id, resource_id) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, collectionId);
            ps.setInt(2, resourceId);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Cannot add resource " + resourceId + " to collection " + collectionId + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean removeResource(int collectionId, int resourceId) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM collection_items WHERE collection_id = ? AND resource_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, collectionId);
            ps.setInt(2, resourceId);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Cannot remove resource " + resourceId + " from collection " + collectionId + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public Collection getById(int collectionId) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM collections WHERE collection_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, collectionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Collection collection = new Collection();
                collection.setCollectionId(rs.getInt("collection_id"));
                collection.setUserId(rs.getInt("user_id"));
                collection.setCollectionName(rs.getString("collection_name"));
                collection.setCreatedAt(rs.getTimestamp("created_at"));
                return collection;
            }
        } catch (SQLException e) {
            System.out.println("Cannot get collection by id " + collectionId + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return null;
    }

}
