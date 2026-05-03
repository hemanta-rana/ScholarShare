package com.ScholarShare.dao;

import com.ScholarShare.entity.Resource;

public interface ResourceDao {
    boolean uploadResource(Resource resource);
    boolean deleteResource(int resourceId);
    boolean rateResource(int resourceId, int userId, int score);
}
