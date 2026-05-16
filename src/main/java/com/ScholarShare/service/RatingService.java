package com.ScholarShare.service;

import com.ScholarShare.dao.RatingDao;
import com.ScholarShare.dao.daoImpl.RatingDaoImpl;
import com.ScholarShare.entity.User;

public class RatingService {

    private final RatingDao ratingDao = new RatingDaoImpl();

    public String submitRating(User loggedUser, int resourceId, int score){
        if (!loggedUser.getRole().equals("student")) {
            return "only student can rate resources ";
        }
        if (score < 1 || score > 5){
            return "invalid score. must be between 1 and 5";
        }
        boolean isSuccess = ratingDao.rateResource(resourceId, loggedUser.getUserId(), score);
        if (isSuccess) return null;
        else return "you have already rated this resource";

    }
}
