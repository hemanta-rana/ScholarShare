package com.ScholarShare.dao;

import com.ScholarShare.entity.Resource;

import java.util.List;

public interface ResourceDao {
    boolean uploadResource(Resource resource);
    boolean deleteResource(int resourceId, int userId, String webappRealPath);
    boolean rateResource(int resourceId, int userId, int score);

    List<Resource> getAllApproved();
    List<Resource> getByFaculty(int facultyId);
    List<Resource> getBySubject(int subjectId);
    List<Resource> getByTopic(int topicId);
    List<Resource> search(String keyword);
    Resource getById(int resourceId);

    default Resource getResourceById(int resourceId) {
        return getById(resourceId);
    }

    default List<Resource> getApprovedResources() {
        return getAllApproved();
    }
}
