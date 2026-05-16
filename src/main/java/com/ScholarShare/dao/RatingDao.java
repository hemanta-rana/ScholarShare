package com.ScholarShare.dao;

public interface RatingDao {
    boolean rateResource(int resourceId, int userId, int score);
    double averageRating(int resourceId);
    boolean hasUserRated(int resourceId, int userId);

}
