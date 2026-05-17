package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.ResourceDao;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.util.DatabaseConnection;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceDaoImpl implements ResourceDao {

    @Override
    public boolean uploadResource(Resource resource) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String sql = "INSERT INTO resources (user_id, topic_id, title, description, file_path, resource_type, status, self_declaration) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resource.getUserId());
            ps.setInt(2, resource.getTopicId());
            ps.setString(3, resource.getTitle().trim());
            ps.setString(4, resource.getDescription());
            ps.setString(5, resource.getFilePath());
            ps.setString(6, resource.getResourceType());
            ps.setString(7, "pending");
            ps.setBoolean(8, resource.isSelfDeclaration());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                conn.commit();
                return true;
            }
            conn.rollback();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("rollback failed " + ex.getMessage());
                }
            }
            System.out.println("uploadResource failed " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public boolean deleteResource(int resourceId, int userId, String webappRealPath) {
        if (resourceId <= 0 || userId <= 0 || webappRealPath == null || webappRealPath.isEmpty()) {
            return false;
        }

        Connection conn = null;
        String filePath = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            String selectSql = "SELECT user_id, file_path FROM resources WHERE resource_id = ?";
            PreparedStatement selectPs = conn.prepareStatement(selectSql);
            selectPs.setInt(1, resourceId);
            ResultSet rs = selectPs.executeQuery();

            if (!rs.next()) {
                conn.rollback();
                return false;
            }

            int ownerId = rs.getInt("user_id");
            filePath = rs.getString("file_path");

            if (ownerId != userId) {
                conn.rollback();
                return false;
            }

            String deleteSql = "DELETE FROM resources WHERE resource_id = ?";
            PreparedStatement deletePs = conn.prepareStatement(deleteSql);
            deletePs.setInt(1, resourceId);
            int rows = deletePs.executeUpdate();

            if (rows > 0) {
                conn.commit();

                if (filePath != null) {
                    File file = new File(webappRealPath, filePath.replace("/", File.separator));
                    if (file.exists()) {
                        file.delete();
                    }
                }
                return true;
            }
            conn.rollback();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("rollback failed " + ex.getMessage());
                }
            }
            System.out.println("deleteResource failed " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return false;
    }

    @Override
    public Resource getResourceById(int resourceId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT r.*, u.full_name AS submitter_name, t.topic_name " +
                    "FROM resources r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "JOIN topics t ON r.topic_id = t.topic_id " +
                    "WHERE r.resource_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, resourceId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
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
                resource.setSubmitterName(rs.getString("submitter_name"));
                resource.setTopicName(rs.getString("topic_name"));
                return resource;
            }
        } catch (SQLException e) {
            System.out.println("getResourceById failed " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return null;
    }

    @Override
    public List<Resource> getResourceByTopicId(int topicId) {
        List<Resource> resources = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT r.*, u.full_name AS submitter_name, t.topic_name " +
                    "FROM resources r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "JOIN topics t ON r.topic_id = t.topic_id " +
                    "WHERE r.topic_id = ? AND r.status = 'approved' " +
                    "ORDER BY r.upload_date DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, topicId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Resource resource = new Resource();
                resource.setResourceId(rs.getInt("resource_id"));
                resource.setTitle(rs.getString("title"));
                resource.setStatus(rs.getString("status"));
                resource.setResourceType(rs.getString("resource_type"));
                resource.setSubmitterName(rs.getString("submitter_name"));
                resource.setTopicName(rs.getString("topic_name"));
                resources.add(resource);
            }
        } catch (SQLException e) {
            System.out.println("getResourceByTopicId failed " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return resources;
    }

    @Override
    public List<Resource> getApprovedResources() {
        List<Resource> resources = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT r.*, u.full_name AS submitter_name, t.topic_name " +
                    "FROM resources r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "JOIN topics t ON r.topic_id = t.topic_id " +
                    "WHERE r.status = 'approved' " +
                    "ORDER BY r.upload_date DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Resource resource = new Resource();
                resource.setResourceId(rs.getInt("resource_id"));
                resource.setTitle(rs.getString("title"));
                resource.setStatus(rs.getString("status"));
                resource.setResourceType(rs.getString("resource_type"));
                resource.setSubmitterName(rs.getString("submitter_name"));
                resource.setTopicName(rs.getString("topic_name"));
                resources.add(resource);
            }
        } catch (SQLException e) {
            System.out.println("getApprovedResources failed " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return resources;
    }

    @Override
    public List<Resource> getResourcesByUser(int userId) {
        List<Resource> resources = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT r.*, u.full_name AS submitter_name, t.topic_name " +
                    "FROM resources r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "JOIN topics t ON r.topic_id = t.topic_id " +
                    "WHERE r.user_id = ? " +
                    "ORDER BY r.upload_date DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Resource resource = new Resource();
                resource.setResourceId(rs.getInt("resource_id"));
                resource.setTitle(rs.getString("title"));
                resource.setStatus(rs.getString("status"));
                resource.setResourceType(rs.getString("resource_type"));
                resource.setSubmitterName(rs.getString("submitter_name"));
                resource.setTopicName(rs.getString("topic_name"));
                resources.add(resource);
            }
        } catch (SQLException e) {
            System.out.println("getResourcesByUser failed " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
        return resources;
    }
}