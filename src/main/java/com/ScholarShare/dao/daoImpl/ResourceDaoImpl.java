package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.ResourceDao;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.Topic;
import com.ScholarShare.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceDaoImpl implements ResourceDao {
    @Override
    public boolean uploadResource(Resource resource) {
        return false;
    }

    @Override
    public boolean deleteResource(int resourceId) {
        return false;
    }

    @Override
    public boolean rateResource(int resourceId, int userId, int score) {
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


}
