package com.ScholarShare.controller;

import com.ScholarShare.dao.StudentDao;
import com.ScholarShare.dao.daoImpl.StudentDaoImpl;
import com.ScholarShare.entity.User;
import com.ScholarShare.service.StudentDashboardService;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@WebServlet("/student/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize       = 5 * 1024 * 1024,
        maxRequestSize    = 10 * 1024 * 1024
)
public class StudentServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads" + File.separator + "profile_pics";
    private static final String DASHBOARD_PATH = "/student/dashboard";

    private StudentDashboardService dashboardService;
    private StudentDao studentDao;

    @Override
    public void init() throws ServletException {
        dashboardService = new StudentDashboardService();
        studentDao = new StudentDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User sessionUser = requireStudent(req, resp);
        if (sessionUser == null) {
            return;
        }

        String action = resolveAction(req);
        switch (action) {
            case "dashboard", "resources", "uploads", "reputation", "analytics" -> handleDashboard(req, resp, sessionUser);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown student page");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User sessionUser = requireStudent(req, resp);
        if (sessionUser == null) {
            return;
        }

        String action = req.getParameter("action");
        if (action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter");
            return;
        }

        switch (action) {
            case "agreeToIntegrityPledge" -> handleIntegrityPledge(req, resp, sessionUser);
            case "flagPlagiarism"         -> handleFlagPlagiarism(req, resp, sessionUser);
            case "uploadProfilePicture"   -> handleUploadProfilePicture(req, resp, sessionUser);
            default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }
    }

    private void handleDashboard(HttpServletRequest req, HttpServletResponse resp, User sessionUser)
            throws ServletException, IOException {

        int userId = sessionUser.getUserId();
        String contextPath = req.getContextPath();
        String searchQuery = req.getParameter("q");

        Map<String, Object> profile = dashboardService.getProfile(userId, contextPath);
        if (profile.isEmpty()) {
            Map<String, Object> fallback = new java.util.HashMap<>();
            fallback.put("id", sessionUser.getUserId());
            fallback.put("name", sessionUser.getFullName());
            fallback.put("firstName", sessionUser.getFullName() != null
                    ? sessionUser.getFullName().trim().split("\\s+")[0] : "");
            fallback.put("email", sessionUser.getEmail());
            fallback.put("initials", sessionUser.getInitials());
            fallback.put("avatarUrl", "");
            fallback.put("reputationScore", 0);
            profile = fallback;
        }

        Map<String, Object> summary = dashboardService.getSummaryCards(userId);
        Map<String, Object> reputation = dashboardService.getReputationBreakdown(userId);
        List<Map<String, Object>> submissions = dashboardService.getRecentSubmissions(userId, searchQuery);
        List<Map<String, Object>> recentResources = dashboardService.getRecentlyAddedResources(userId);

        int pendingReview = (int) summary.getOrDefault("pendingReview", 0);
        String welcomeMessage = dashboardService.buildWelcomeMessage(
                (String) profile.get("firstName"), pendingReview);

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("profilePicPath") != null) {
            profile.put("avatarUrl", contextPath + "/" + session.getAttribute("profilePicPath"));
        }

        req.setAttribute("profile", profile);
        req.setAttribute("summary", summary);
        req.setAttribute("reputation", reputation);
        req.setAttribute("submissions", submissions);
        req.setAttribute("recentResources", recentResources);
        req.setAttribute("welcomeMessage", welcomeMessage);
        req.setAttribute("searchQuery", searchQuery != null ? searchQuery : "");
        req.setAttribute("activePage", resolveActivePage(req));

        Object flashMessage = session != null ? session.getAttribute("flashMessage") : null;
        Object flashError = session != null ? session.getAttribute("flashError") : null;
        if (flashMessage != null) {
            req.setAttribute("flashMessage", flashMessage);
            session.removeAttribute("flashMessage");
        }
        if (flashError != null) {
            req.setAttribute("flashError", flashError);
            session.removeAttribute("flashError");
        }

        req.getRequestDispatcher("/WEB-INF/views/student-dashboard.jsp").forward(req, resp);
    }

    private String resolveActivePage(HttpServletRequest req) {
        String action = resolveAction(req);
        return switch (action) {
            case "resources" -> "resources";
            case "uploads" -> "uploads";
            case "reputation" -> "reputation";
            case "analytics" -> "analytics";
            default -> "dashboard";
        };
    }

    private void handleIntegrityPledge(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        HttpSession session = req.getSession(false);
        boolean success = studentDao.agreeToIntegrityPledge(user.getUserId());
        if (session != null) {
            if (success) {
                session.setAttribute("flashMessage", "Integrity pledge recorded successfully.");
            } else {
                session.setAttribute("flashError", "Could not record pledge. You may have already signed it.");
            }
        }
        resp.sendRedirect(dashboardUrl(req));
    }

    private void handleFlagPlagiarism(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        HttpSession session = req.getSession(false);
        String resourceIdParam = req.getParameter("resourceId");
        String reason = req.getParameter("reason");

        if (resourceIdParam == null || reason == null || reason.trim().isEmpty()) {
            if (session != null) {
                session.setAttribute("flashError", "Resource ID and reason are required.");
            }
            resp.sendRedirect(dashboardUrl(req));
            return;
        }

        try {
            int resourceId = Integer.parseInt(resourceIdParam);
            boolean success = studentDao.flagResourceForPlagiarism(resourceId, user.getUserId(), reason.trim());
            if (session != null) {
                if (success) {
                    session.setAttribute("flashMessage", "Resource flagged for review. Thank you.");
                } else {
                    session.setAttribute("flashError", "Could not submit flag. Please try again.");
                }
            }
        } catch (NumberFormatException e) {
            if (session != null) {
                session.setAttribute("flashError", "Invalid resource ID.");
            }
        }
        resp.sendRedirect(dashboardUrl(req));
    }

    private void handleUploadProfilePicture(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        Part filePart = req.getPart("profilePic");
        if (filePart == null || filePart.getSize() == 0) {
            if (session != null) {
                session.setAttribute("flashError", "Please select a file to upload.");
            }
            resp.sendRedirect(dashboardUrl(req));
            return;
        }

        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            if (session != null) {
                session.setAttribute("flashError", "Only image files are allowed.");
            }
            resp.sendRedirect(dashboardUrl(req));
            return;
        }

        int userId = user.getUserId();
        String originalName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String savedFileName = userId + "_" + originalName;
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, Paths.get(uploadPath, savedFileName), StandardCopyOption.REPLACE_EXISTING);
        }

        String relativePath = UPLOAD_DIR.replace(File.separatorChar, '/') + "/" + savedFileName;
        boolean success = studentDao.updateProfilePicture(userId, relativePath);

        if (session != null) {
            if (success) {
                session.setAttribute("profilePicPath", relativePath);
                user.setProfilePic(relativePath);
                session.setAttribute("user", user);
                session.setAttribute("flashMessage", "Profile picture updated successfully.");
            } else {
                session.setAttribute("flashError", "File saved but profile update failed.");
            }
        }
        resp.sendRedirect(dashboardUrl(req));
    }

    private User requireStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        Object value = session.getAttribute("user");
        if (!(value instanceof User user)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        if (!user.isStudent()) {
            resp.sendRedirect(req.getContextPath() + (user.isAdmin() ? "/admin/dashboard" : "/home"));
            return null;
        }
        return user;
    }

    private String resolveAction(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty() && !"/".equals(pathInfo)) {
            String segment = pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo;
            int slash = segment.indexOf('/');
            if (slash >= 0) {
                segment = segment.substring(0, slash);
            }
            return segment;
        }
        String action = req.getParameter("action");
        return action != null ? action : "dashboard";
    }

    private String dashboardUrl(HttpServletRequest req) {
        return req.getContextPath() + DASHBOARD_PATH;
    }
}
