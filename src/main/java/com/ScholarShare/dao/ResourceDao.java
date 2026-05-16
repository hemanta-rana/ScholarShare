package com.ScholarShare.dao;

import com.ScholarShare.entity.Resource;

import java.util.List;

public interface ResourceDao {
    boolean uploadResource(Resource resource);
    boolean deleteResource(int resourceId);
    Resource getResourceById(int resourceId);
    List<Resource> getResourceByTopicId(int topicId);
    List<Resource> getApprovedResources();
    List<Resource> getResourcesByUser(int userId);
}
