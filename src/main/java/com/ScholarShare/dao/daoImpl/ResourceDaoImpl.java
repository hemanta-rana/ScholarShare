package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.ResourceDao;
import com.ScholarShare.entity.Resource;

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
    public Resource getResourceById(int resourceId) {
        return null;
    }

    @Override
    public List<Resource> getResourceByTopicId(int topicId) {
        return List.of();
    }

    @Override
    public List<Resource> getApprovedResources() {
        return List.of();
    }

    @Override
    public List<Resource> getResourcesByUser(int userId) {
        return List.of();
    }
}
