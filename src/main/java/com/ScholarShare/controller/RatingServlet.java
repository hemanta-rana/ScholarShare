package com.ScholarShare.controller;

import com.ScholarShare.entity.User;
import com.ScholarShare.service.RatingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/rating")
public class RatingServlet extends HttpServlet {

    private final RatingService ratingService = new RatingService();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = (User) session.getAttribute("user");
        try {
            int resourceId = Integer.parseInt(request.getParameter("resourceId"));
            int score = Integer.parseInt(request.getParameter("score"));

            String resultMessage = ratingService.submitRating(user, resourceId, score);
            if (resultMessage != null) {
                session.setAttribute("resultMessage", resultMessage);
            }
            response.sendRedirect(request.getContextPath() + "/resource-detail?id=" + resourceId);
        } catch (NumberFormatException e) {
            session.setAttribute("error", "invalid rating");
            response.sendRedirect(request.getContextPath() + "/home");
        }



    }

}
