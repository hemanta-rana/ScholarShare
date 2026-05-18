package com.ScholarShare.dao;

import com.ScholarShare.entity.Flag;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.User;

import java.util.List;
import java.util.Map;

public interface AdminDao {
    int getNumberOfPendingRegistration();
    int getPendingSubmissions();
    int getNumberOfOpenFlags();
    int getTotalResources();
    int getApprovalRate();
    List<Resource> getRecentSubmission();
    List<User> getPendingUserRegistration();
    List<Flag> getRecentFlags();
    Map<String, Integer> getWeeklySubmissionCounts();

    List<User> getAllPendingRegistrations();
    boolean updateUserStatus(int userId, String status);
    List<Resource> getPipelineResources();
    boolean updateResourceStatus(int resourceId, String status);
    List<Flag> getAllFlags();
    boolean updateFlagStatus(int flagId, String status);
    List<Map<String, Object>> getTopContributors(int limit);
    List<Map<String, Object>> getMostFlaggedResources(int limit);

    // ── Admin profile ──
    User getAdminById(int userId);
    boolean updateAdminProfile(int userId, String fullName, String phone);
    boolean uploadAdminProfilePicture(int userId, String relativePath);
}
