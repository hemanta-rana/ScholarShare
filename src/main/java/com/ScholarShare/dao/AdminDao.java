package com.ScholarShare.dao;

import com.ScholarShare.entity.Flag;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.User;

import java.util.List;

public interface AdminDao {
    int getNumberOfPendingRegistration();
    int getPendingSubmissions();
    int getNumberOfOpenFlags();
    int getTotalResources();
    int getApprovalRate();
    List<Resource> getRecentSubmission();
    List<User> getPendingUserRegistration();
    List<Flag> getRecentFlags();

}
