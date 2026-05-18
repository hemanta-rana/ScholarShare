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

@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends HttpServlet {

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
        String searchQuery = req.getParameter("q");

        jakarta.servlet.http.HttpSession session = req.getSession(false);
        if (session != null) {
            Object flash = session.getAttribute("flashMessage");
            if (flash != null) {
                req.setAttribute("flashMessage", flash);
                session.removeAttribute("flashMessage");
            }
            Object flashErr = session.getAttribute("flashError");
            if (flashErr != null) {
                req.setAttribute("flashError", flashErr);
                session.removeAttribute("flashError");
            }
        }

        Map<String, Object> profile = dashboardService.getProfile(userId, contextPath);
        if (profile.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Map<String, Object> summary = dashboardService.getSummaryCards(userId);
        Map<String, Object> reputation = dashboardService.getReputationBreakdown(userId);
        int pendingReview = (Integer) summary.getOrDefault("pendingReview", 0);
        String welcomeMessage = dashboardService.buildWelcomeMessage((String) profile.get("firstName"), pendingReview);
        List<Map<String, Object>> submissions = dashboardService.getRecentSubmissions(userId, searchQuery);
        List<Map<String, Object>> recentResources = dashboardService.getRecentlyAddedResources(userId);

        req.setAttribute("profile", profile);
        req.setAttribute("summary", summary);
        req.setAttribute("reputation", reputation);
        req.setAttribute("welcomeMessage", welcomeMessage);
        req.setAttribute("submissions", submissions);
        req.setAttribute("recentResources", recentResources);
        req.setAttribute("activePage", "dashboard");
        req.setAttribute("searchQuery", searchQuery != null ? searchQuery : "");

        req.getRequestDispatcher("/WEB-INF/views/student-dashboard.jsp").forward(req, resp);
    }
}
