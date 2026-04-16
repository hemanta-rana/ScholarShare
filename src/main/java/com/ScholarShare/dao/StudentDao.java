package com.ScholarShare.dao;

import com.ScholarShare.entity.Collection;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.User;

import java.util.List;

public interface StudentDao {
    User getUserById(int userId);
    User getUserByEmail(String email);
    List<Collection>getStudentCollections(int userId);
    boolean agreeToIntegrityPledge (int userId);
    boolean uploadResource(Resource resource);
    boolean deleteResource(int resourceId);
    int getContributorReputationScore(int userId);
    boolean createCollection(int userId, String collectionName);
    boolean addResourceToCollection(int collectionId, int resourceId);
    boolean deleteCollection(int collectionId);
    boolean deleteResourceFromCollection(int collectionId, int resourceId);
    boolean flagResourceForPlagiarism(int resourceId,int userId, String reason);
    boolean rateResource(int resourceId, int userId, int score);


}
