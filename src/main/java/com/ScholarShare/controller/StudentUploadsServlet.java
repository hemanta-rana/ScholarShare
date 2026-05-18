package com.ScholarShare.controller;

import com.ScholarShare.dao.daoImpl.StudentDaoImpl;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.User;
import com.ScholarShare.service.StudentDashboardService;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@WebServlet("/student/uploads")
public class StudentUploadsServlet extends HttpServlet {

    private final StudentDashboardService dashboardService = new StudentDashboardService();
    private final StudentDaoImpl studentDao = new StudentDaoImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) SessionUtil.getAttribute(req, "user");
        if (user == null || !user.isStudent()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int userId = user.getUserId();

        /* Flash messages */
        HttpSession session = req.getSession(false);
        if (session != null) {
            Object flash = session.getAttribute("flashMessage");
            if (flash != null) { req.setAttribute("flashMessage", flash); session.removeAttribute("flashMessage"); }
            Object flashErr = session.getAttribute("flashError");
            if (flashErr != null) { req.setAttribute("flashError", flashErr); session.removeAttribute("flashError"); }
        }

        /* Status filter from query param — "all" | "approved" | "pending" | "review" | "rejected" */
        String statusFilter = req.getParameter("status");
        if (statusFilter == null || statusFilter.isBlank()) statusFilter = "all";

        /* Search query */
        String searchQuery = req.getParameter("q");

        /* Load all submissions (no limit) */
        List<Resource> resources;
        if (searchQuery != null && !searchQuery.isBlank()) {
            resources = studentDao.searchStudentSubmissions(userId, searchQuery.trim(), 200);
        } else {
            resources = studentDao.getRecentSubmissionsByUser(userId, 200);
        }

        /* Map to presentation rows and apply status filter */
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        List<Map<String, Object>> rows = new ArrayList<>();

        for (Resource r : resources) {
            String rawStatus = r.getStatus() != null ? r.getStatus() : "pending";
            String statusClass = statusClass(rawStatus);

            /* Apply filter */
            if (!"all".equals(statusFilter) && !statusFilter.equals(statusClass)) continue;

            Map<String, Object> row = new HashMap<>();
            row.put("id",          r.getResourceId());
            row.put("title",       r.getTitle());
            row.put("type",        formatType(r.getResourceType()));
            row.put("subject",     r.getSubjectName() != null ? r.getSubjectName() : "—");
            row.put("date",        r.getUploadDate() != null ? sdf.format(r.getUploadDate()) : "—");
            row.put("status",      rawStatus.replace("_", " ").toUpperCase(Locale.ENGLISH));
            row.put("statusClass", statusClass);
            rows.add(row);
        }

        /* Count per status for the filter tabs */
        Map<String, Integer> counts = studentDao.countUploadsByStatus(userId);
        int totalCount    = resources.size();
        int approvedCount = counts.getOrDefault("approved", 0);
        int pendingCount  = counts.getOrDefault("pending", 0)
                          + counts.getOrDefault("underReview", 0);
        int rejectedCount = counts.getOrDefault("rejected", 0);

        req.setAttribute("profile",       dashboardService.getProfile(userId, req.getContextPath()));
        req.setAttribute("uploads",       rows);
        req.setAttribute("statusFilter",  statusFilter);
        req.setAttribute("searchQuery",   searchQuery != null ? searchQuery : "");
        req.setAttribute("totalCount",    totalCount);
        req.setAttribute("approvedCount", approvedCount);
        req.setAttribute("pendingCount",  pendingCount);
        req.setAttribute("rejectedCount", rejectedCount);

        req.getRequestDispatcher("/WEB-INF/views/my-uploads.jsp").forward(req, resp);
    }

    private String statusClass(String raw) {
        return switch (raw) {
            case "approved"    -> "approved";
            case "rejected"    -> "rejected";
            case "under_review"-> "review";
            default            -> "pending";
        };
    }

    private String formatType(String t) {
        if (t == null) return "DOCUMENT";
        return switch (t) {
            case "notes"          -> "Notes";
            case "past_paper"     -> "Past Paper";
            case "summary"        -> "Summary";
            case "revision_guide" -> "Revision Guide";
            default               -> t.replace("_", " ");
        };
    }
}
