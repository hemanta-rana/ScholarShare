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

    boolean updateProfilePicture(int userId, String profilePicPath);

    Map<String, Integer> countUploadsByStatus(int userId);

    int countSavedResources(int userId);

    int countApprovedUploadsSince(int userId, int daysBack);

    List<Resource> getRecentSubmissionsByUser(int userId, int limit);

    List<Resource> getRecentlyAddedApprovedResources(int excludeUserId, int limit);

    List<Resource> searchStudentSubmissions(int userId, String query, int limit);

    double getAccuracyRate(int userId);

    double getPeerHelpfulness(int userId);

    int getContributorPercentileRank(int userId);
}
