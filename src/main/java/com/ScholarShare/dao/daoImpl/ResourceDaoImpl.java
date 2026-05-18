package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.ResourceDao;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.Topic;
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
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO resources (user_id, topic_id, title, description, file_path, "
                    + "resource_type, status, self_declaration) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, resource.getUserId());
            ps.setInt(2, resource.getTopicId());
            ps.setString(3, resource.getTitle());
            ps.setString(4, resource.getDescription());
            ps.setString(5, resource.getFilePath());
            ps.setString(6, resource.getResourceType());
            ps.setString(7, resource.getStatus() != null ? resource.getStatus() : "pending");
            ps.setBoolean(8, resource.isSelfDeclaration());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Cannot upload resource " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean deleteResource(int resourceId, int userId, String webappRealPath) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sel = "SELECT user_id, file_path FROM resources WHERE resource_id = ?";
            PreparedStatement ps = connection.prepareStatement(sel);
            ps.setInt(1, resourceId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return false;
            }
            if (rs.getInt("user_id") != userId) {
                return false;
            }
            String relativePath = rs.getString("file_path");
            rs.close();
            ps.close();

            String del = "DELETE FROM resources WHERE resource_id = ? AND user_id = ?";
            PreparedStatement delPs = connection.prepareStatement(del);
            delPs.setInt(1, resourceId);
            delPs.setInt(2, userId);
            int rows = delPs.executeUpdate();
            delPs.close();

            if (rows > 0 && relativePath != null && !relativePath.isBlank() && webappRealPath != null
                    && !webappRealPath.isBlank()) {
                File f = new File(webappRealPath, relativePath.replace("/", File.separator));
                if (f.exists() && f.isFile()) {
                    f.delete();
                }
            }
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Cannot delete resource " + resourceId + " " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean rateResource(int resourceId, int userId, int score) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO ratings (resource_id, user_id, score) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, resourceId);
            ps.setInt(2, userId);
            ps.setInt(3, score);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Failed to save rating " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public List<Resource> getAllApproved() {
        List<Resource> resources = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql ="SELECT * FROM resources WHERE status = 'approved' ORDER BY upload_date DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
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
                resources.add(resource);
            }
        } catch (SQLException e){
            System.out.println("Cannot get all approved resources "+e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return resources;
    }

    @Override
    public List<Resource> getByFaculty(int facultyId){
        List<Resource> resources = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql ="SELECT r.* FROM resources r " +
                    "JOIN topics t ON r.topic_id = t.topic_id " +
                    "JOIN subjects s ON t.subject_id = s.subject_id " +
                    "WHERE s.faculty_id = ? AND r.status = 'approved' " +
                    "ORDER BY r.upload_date DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, facultyId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
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
                resources.add(resource);
            }
        } catch (SQLException e){
            System.out.println("Cannot get resource by faculty id "+e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return resources;
    }

    @Override
    public List<Resource> getBySubject(int subjectId) {
        List<Resource> resources = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT r.* FROM resources r " +
                    "JOIN topics t ON r.topic_id = t.topic_id " +
                    "WHERE t.subject_id = ? AND r.status = 'approved' " +
                    "ORDER BY r.upload_date DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, subjectId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
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
                resources.add(resource);
            }
        } catch (SQLException e) {
            System.out.println("Cannot get resource by subject id " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return resources;
    }

    @Override
    public List<Resource> getByTopic(int topicId) {
        List<Resource> resources = new ArrayList<>();
        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM resources " +
                    "WHERE topic_id = ? AND status = 'approved' " +
                    "ORDER BY upload_date DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,topicId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
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
                resources.add(resource);
            }
        } catch (SQLException e){
            System.out.println("Cannot get resource by topic id " + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return resources;
    }

    @Override
    public List<Resource> search(String keyword) {
        List<Resource> resources = new ArrayList<>();
        Connection connection = null;
        try{
            connection = DatabaseConnection.getConnection();
            String sql ="SELECT * FROM resources " +
                    "WHERE status = 'approved' " +
                    "AND (title LIKE ? OR description LIKE ?) " +
                    "ORDER BY upload_date DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + keyword + "%");
            statement.setString(2, "%" + keyword + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
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
                resources.add(resource);
            }
        } catch (SQLException e){
            System.out.println("Cannot get resource by keyword " + keyword + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        }
        return resources;
    }

    @Override
    public Resource getById(int resourceId) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM resources WHERE resource_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, resourceId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
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
        } catch (SQLException e){
            System.out.println("Cannot get resource by id " + resourceId + e.getMessage());
        } finally {
            DatabaseConnection.closeConnection(connection);
        } return null;
    }
}
