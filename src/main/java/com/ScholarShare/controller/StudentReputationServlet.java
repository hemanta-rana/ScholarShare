package com.ScholarShare.controller;

import com.ScholarShare.entity.User;
import com.ScholarShare.service.StudentDashboardService;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/student/reputation")
public class StudentReputationServlet extends HttpServlet {

    private final StudentDashboardService dashboardService = new StudentDashboardService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) SessionUtil.getAttribute(req, "user");
        if (user == null || !user.isStudent()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userId = user.getUserId();
        String contextPath = req.getContextPath();

        Map<String, Object> profile = dashboardService.getProfile(userId, contextPath);
        if (profile.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Map<String, Object> reputation = dashboardService.getReputationBreakdown(userId);
        Map<String, Object> summary = dashboardService.getSummaryCards(userId);

        req.setAttribute("profile", profile);
        req.setAttribute("reputation", reputation);
        req.setAttribute("summary", summary);
        req.setAttribute("activePage", "reputation");

        req.getRequestDispatcher("/WEB-INF/views/student-reputation.jsp").forward(req, resp);
    }
}
