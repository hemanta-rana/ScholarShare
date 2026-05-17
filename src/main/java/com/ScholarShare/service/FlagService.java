package com.ScholarShare.service;

import com.ScholarShare.dao.FlagDao;
import com.ScholarShare.dao.ResourceDao;
import com.ScholarShare.dao.daoImpl.FlagDaoImpl;
import com.ScholarShare.dao.daoImpl.ResourceDaoImpl;
import com.ScholarShare.entity.Flag;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.util.ValidationUtil;

public class FlagService {

    private final FlagDao flagDao = new FlagDaoImpl();
    private final ResourceDao resourceDao = new ResourceDaoImpl();

    public String flagResource(int studentId, int resourceId, String reason) {

        if (ValidationUtil.isNullOrEmpty(reason)) {
            return "Reason is required.";
        }

        Resource resource = resourceDao.getResourceById(resourceId);
        if (resource == null) {
            return "Resource not found.";
        }

        if (flagDao.hasStudentAlreadyFlagged(resourceId, studentId)) {
            return "You have already flagged this resource.";
        }

        Flag flag = new Flag();
        flag.setResourceId(resourceId);
        flag.setFlaggedBy(studentId);
        flag.setReason(reason.trim());

        if (flagDao.createFlag(flag)) {
            return null;
        }

        return "Failed to submit the flag.";
    }
}
