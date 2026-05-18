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
import java.util.List;
import java.util.Map;

@WebServlet("/student/recent-resources")
public class StudentRecentResourcesServlet extends HttpServlet {

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

        List<Map<String, Object>> recentResources = dashboardService.getRecentlyAddedResources(userId);

        req.setAttribute("profile", profile);
        req.setAttribute("recentResources", recentResources);
        req.setAttribute("activePage", "recent-resources");

        req.getRequestDispatcher("/WEB-INF/views/student-recent-resources.jsp").forward(req, resp);
    }
}
