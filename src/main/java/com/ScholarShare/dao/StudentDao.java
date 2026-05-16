package com.ScholarShare.dao;

import com.ScholarShare.entity.User;

public interface StudentDao {

    User getUserById(int userId);
    User getStudentByEmail(String email);
    boolean agreeToIntegrityPledge(int userId);
    int getContributorReputationScore(int userId);
    boolean flagResourceForPlagiarism(int resourceId, int userId, String reason);
    boolean uploadProfilePicture(int resourceId, int userId);

    // -- new reputation methods --
    String  getContributorStatus(int userId);
    boolean addReputationEventOnUpload(int userId);
    boolean addReputationEventOnDownload(int contributorUserId);
    boolean addReputationEventOnLike(int contributorUserId);
}