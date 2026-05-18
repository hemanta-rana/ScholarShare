package com.ScholarShare.dao;

import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.User;

import java.util.List;
import java.util.Map;

public interface StudentDao {

    User getUserById(int userId);
    User getStudentByEmail(String email);
    boolean agreeToIntegrityPledge(int userId);
    int getContributorReputationScore(int userId);
    boolean flagResourceForPlagiarism(int resourceId, int userId, String reason);
    boolean uploadProfilePicture(int userId, String relativePath);

    Map<String, Integer> countUploadsByStatus(int userId);
    int countApprovedUploadsSince(int userId, int daysBack);
    int countSavedResources(int userId);
    double getAccuracyRate(int userId);
    double getPeerHelpfulness(int userId);
    int getContributorPercentileRank(int userId);
    List<Resource> searchStudentSubmissions(int userId, String query, int limit);
    List<Resource> getRecentSubmissionsByUser(int userId, int limit);
    List<Resource> getRecentlyAddedApprovedResources(int userId, int limit);

    // -- new reputation methods --
    String getContributorStatus(int userId);
    boolean addReputationEventOnUpload(int userId);
    boolean addReputationEventOnDownload(int contributorUserId);
    boolean addReputationEventOnLike(int contributorUserId);

    // -- profile update --
    boolean updateProfile(int userId, String fullName, String phone);
}
