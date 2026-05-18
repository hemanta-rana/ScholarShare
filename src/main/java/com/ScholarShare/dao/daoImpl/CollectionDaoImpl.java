package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.CollectionDao;
import com.ScholarShare.entity.Collection;
import com.ScholarShare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CollectionDaoImpl implements CollectionDao {

    private static final String DEFAULT_COLLECTION_NAME = "Saved";

    @Override
    public boolean createCollection(int userId, String collectionName) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO collections (user_id, collection_name) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, collectionName);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("cannot create collection for student id " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean deleteCollection(int collectionId) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM collections WHERE collection_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, collectionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("cannot delete collection id " + collectionId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean addResourceToCollection(int userId, int resourceId) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            int collectionId = getOrCreateDefaultCollection(connection, userId);
            if (collectionId <= 0) {
                return false;
            }

            if (resourceExistsInCollection(connection, collectionId, resourceId)) {
                return true;
            }

            String sql = "INSERT INTO collection_items (collection_id, resource_id) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, collectionId);
            ps.setInt(2, resourceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("failed to add resource to collection for user " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean deleteResourceFromCollection(int collectionId, int resourceId) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM collection_items WHERE collection_id = ? AND resource_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, collectionId);
            ps.setInt(2, resourceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("failed to delete resource from collection " + collectionId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public List<Collection> getStudentCollections(int userId) {
        List<Collection> collections = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM collections WHERE user_id = ? ORDER BY created_at DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Collection collection = new Collection();
                collection.setCollectionId(rs.getInt("collection_id"));
                collection.setCollectionName(rs.getString("collection_name"));
                collection.setCreatedAt(rs.getTimestamp("created_at"));
                collections.add(collection);
            }
        } catch (SQLException e) {
            System.out.println("cannot get collections for student id " + userId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return collections;
    }

    private int getOrCreateDefaultCollection(Connection connection, int userId) throws SQLException {
        String selectSql = "SELECT collection_id FROM collections WHERE user_id = ? AND collection_name = ? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(selectSql)) {
            ps.setInt(1, userId);
            ps.setString(2, DEFAULT_COLLECTION_NAME);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("collection_id");
            }
        }

        String insertSql = "INSERT INTO collections (user_id, collection_name) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setString(2, DEFAULT_COLLECTION_NAME);
            if (ps.executeUpdate() > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    private boolean resourceExistsInCollection(Connection connection, int collectionId, int resourceId)
            throws SQLException {
        String sql = "SELECT 1 FROM collection_items WHERE collection_id = ? AND resource_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, collectionId);
            ps.setInt(2, resourceId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }
}
