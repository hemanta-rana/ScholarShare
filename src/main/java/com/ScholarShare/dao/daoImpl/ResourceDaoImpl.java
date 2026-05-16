package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.ResourceDao;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.util.DatabaseConnection;
import com.ScholarShare.util.ValidationUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceDaoImpl implements ResourceDao {

    private static final String INSERT_RESOURCE_SQL =
            "INSERT INTO resources (user_id, topic_id, title, description, file_path, resource_type, status, self_declaration) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID_SQL =
            "SELECT r.*, u.full_name AS submitter_name, t.topic_name " +
                    "FROM resources r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "JOIN topics t ON r.topic_id = t.topic_id " +
                    "WHERE r.resource_id = ?";

    private static final String SELECT_BY_TOPIC_SQL =
            "SELECT r.*, u.full_name AS submitter_name, t.topic_name " +
                    "FROM resources r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "JOIN topics t ON r.topic_id = t.topic_id " +
                    "WHERE r.topic_id = ? AND r.status = 'approved' " +
                    "ORDER BY r.upload_date DESC";

    private static final String SELECT_APPROVED_SQL =
            "SELECT r.*, u.full_name AS submitter_name, t.topic_name " +
                    "FROM resources r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "JOIN topics t ON r.topic_id = t.topic_id " +
                    "WHERE r.status = 'approved' " +
                    "ORDER BY r.upload_date DESC";

    private static final String SELECT_BY_USER_SQL =
            "SELECT r.*, u.full_name AS submitter_name, t.topic_name " +
                    "FROM resources r " +
                    "JOIN users u ON r.user_id = u.user_id " +
                    "JOIN topics t ON r.topic_id = t.topic_id " +
                    "WHERE r.user_id = ? " +
                    "ORDER BY r.upload_date DESC";

    private static final String SELECT_OWNER_AND_PATH_SQL =
            "SELECT user_id, file_path FROM resources WHERE resource_id = ?";

    private static final String DELETE_RESOURCE_SQL =
            "DELETE FROM resources WHERE resource_id = ?";

    @Override
    public boolean uploadResource(Resource resource) {
        if (!isValidForUpload(resource)) {
            return false;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(INSERT_RESOURCE_SQL)) {
                ps.setInt(1, resource.getUserId());
                ps.setInt(2, resource.getTopicId());
                ps.setString(3, resource.getTitle().trim());
                ps.setString(4, resource.getDescription());
                ps.setString(5, resource.getFilePath());
                ps.setString(6, resource.getResourceType());
                ps.setString(7, resource.getStatus() != null ? resource.getStatus() : "pending");
                ps.setBoolean(8, resource.isSelfDeclaration());

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    conn.commit();
                    return true;
                }
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("rollback failed: " + ex.getMessage());
                }
            }
            System.out.println("uploadResource failed: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public boolean deleteResource(int resourceId, int userId, String webappRealPath) {
        if (resourceId <= 0 || userId <= 0 || ValidationUtil.isNullOrEmpty(webappRealPath)) {
            return false;
        }
        return deleteResourceInternal(resourceId, userId, webappRealPath, true);
    }

    @Override
    public boolean deleteResource(int resourceId, String webappRealPath) {
        if (resourceId <= 0 || ValidationUtil.isNullOrEmpty(webappRealPath)) {
            return false;
        }
        return deleteResourceInternal(resourceId, -1, webappRealPath, false);
    }

    private boolean deleteResourceInternal(int resourceId, int userId, String webappRealPath, boolean checkOwnership) {
        Connection conn = null;
        String relativeFilePath = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            int ownerId;
            try (PreparedStatement selectPs = conn.prepareStatement(SELECT_OWNER_AND_PATH_SQL)) {
                selectPs.setInt(1, resourceId);
                try (ResultSet rs = selectPs.executeQuery()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    ownerId = rs.getInt("user_id");
                    relativeFilePath = rs.getString("file_path");

                    if (checkOwnership && ownerId != userId) {
                        conn.rollback();
                        return false;
                    }
                }
            }

            try (PreparedStatement deletePs = conn.prepareStatement(DELETE_RESOURCE_SQL)) {
                deletePs.setInt(1, resourceId);
                int rows = deletePs.executeUpdate();
                if (rows <= 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();

            if (relativeFilePath != null) {
                deleteFileFromDisk(webappRealPath, relativeFilePath);
            }
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("rollback failed: " + ex.getMessage());
                }
            }
            System.out.println("deleteResource failed: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    private void deleteFileFromDisk(String webappRealPath, String relativeFilePath) {
        String normalized = relativeFilePath.replace("/", File.separator).replace("\\", File.separator);
        File file = new File(webappRealPath, normalized);
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                System.out.println("warning: could not delete file " + file.getAbsolutePath());
            }
        }
    }

    @Override
    public Resource getResourceById(int resourceId) {
        if (resourceId <= 0) {
            return null;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, resourceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResource(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("getResourceById failed: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Resource> getResourceByTopicId(int topicId) {
        List<Resource> resources = new ArrayList<>();
        if (topicId <= 0) {
            return resources;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_TOPIC_SQL)) {
            ps.setInt(1, topicId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resources.add(mapResource(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("getResourceByTopicId failed: " + e.getMessage());
        }
        return resources;
    }

    @Override
    public List<Resource> getApprovedResources() {
        List<Resource> resources = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_APPROVED_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resources.add(mapResource(rs));
            }
        } catch (SQLException e) {
            System.out.println("getApprovedResources failed: " + e.getMessage());
        }
        return resources;
    }

    @Override
    public List<Resource> getResourcesByUser(int userId) {
        List<Resource> resources = new ArrayList<>();
        if (userId <= 0) {
            return resources;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_USER_SQL)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resources.add(mapResource(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("getResourcesByUser failed: " + e.getMessage());
        }
        return resources;
    }

    private boolean isValidForUpload(Resource resource) {
        if (resource == null) {
            return false;
        }
        if (resource.getUserId() <= 0 || resource.getTopicId() <= 0) {
            return false;
        }
        if (ValidationUtil.isNullOrEmpty(resource.getTitle())) {
            return false;
        }
        if (ValidationUtil.isNullOrEmpty(resource.getFilePath())) {
            return false;
        }
        if (ValidationUtil.isNullOrEmpty(resource.getResourceType())) {
            return false;
        }
        if (!resource.isSelfDeclaration()) {
            return false;
        }
        return isAllowedResourceType(resource.getResourceType());
    }

    private boolean isAllowedResourceType(String resourceType) {
        return "notes".equals(resourceType)
                || "past_paper".equals(resourceType)
                || "summary".equals(resourceType)
                || "revision_guide".equals(resourceType)
                || "other".equals(resourceType);
    }

    private Resource mapResource(ResultSet rs) throws SQLException {
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
}