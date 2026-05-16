package com.ScholarShare.dao.daoImpl;

import com.ScholarShare.dao.ResourceDao;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.Topic;

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


}
