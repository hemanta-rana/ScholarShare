package com.ScholarShare.dao;

import com.ScholarShare.entity.Collection;

import java.util.List;

public interface CollectionDao {
    boolean createCollection(int userId, String collectionName) ;
    boolean deleteCollection(int collectionId);
     boolean addResourceToCollection(int userId, int resourceId);
    boolean deleteResourceFromCollection(int collectionId, int resourceId);
    List<Collection> getStudentCollections(int userId);
}
