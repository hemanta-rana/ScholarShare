package com.ScholarShare.controller;

import com.ScholarShare.entity.User;
import com.ScholarShare.service.ResourceService;
import com.ScholarShare.service.StudentDashboardService;
import com.ScholarShare.util.FileUploadUtil;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@WebServlet("/student/upload-resource")
@MultipartConfig(maxFileSize = 1024 * 1024 * 50, maxRequestSize = 1024 * 1024 * 55)
public class ResourceUploadServlet extends HttpServlet {

    private final ResourceService resourceService = new ResourceService();
    private final StudentDashboardService dashboardService = new StudentDashboardService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) SessionUtil.getAttribute(req, "user");
        if (user == null || !user.isStudent()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        prepareUploadView(req, user);
        req.getRequestDispatcher("/WEB-INF/views/upload-resource.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) SessionUtil.getAttribute(req, "user");
        if (user == null || !user.isStudent()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String title = req.getParameter("title");
        String courseCode = req.getParameter("courseCode");
        String description = req.getParameter("description");
        String resourceType = req.getParameter("resourceType");
        String topicCategory = req.getParameter("topicCategory");
        boolean pledgeAgreed = "yes".equals(req.getParameter("pledgeAgreed"));
        Part filePart = req.getPart("resourceFile");

        if (courseCode != null && !courseCode.isBlank()) {
            String prefix = "Course: " + courseCode.trim();
            if (description == null || description.isBlank()) {
                description = prefix;
            } else if (!description.trim().startsWith("Course:")) {
                description = prefix + "\n" + description.trim();
            }
        }

        prepareUploadView(req, user);

        String originalName = "";
        long fileSize = 0;
        if (filePart != null) {
            fileSize = filePart.getSize();
            if (filePart.getSubmittedFileName() != null) {
                originalName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            }
        }

        String error = resourceService.validateUpload(title, description, resourceType,
                topicCategory, pledgeAgreed, originalName, fileSize);

        if (error != null) {
            req.setAttribute("error", error);
            repopulateForm(req, title, courseCode, description, resourceType, topicCategory);
            req.getRequestDispatcher("/WEB-INF/views/upload-resource.jsp").forward(req, resp);
            return;
        }

        String datePrefix = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // Sanitise filename — strip path separators and non-safe characters
        String safeName = originalName.toLowerCase().replaceAll("[^a-zA-Z0-9._-]", "_");
        String fileName = datePrefix + "_" + safeName;

        File uploadDir = FileUploadUtil.resolveUploadDir(getServletContext(), "uploads/resources");
        String savedPath = uploadDir.getAbsolutePath() + File.separator + fileName;
        filePart.write(savedPath);

        String relativePath = "uploads/resources/" + fileName;
        boolean saved = resourceService.saveUploadedResource(user.getUserId(), title, description,
                resourceType, topicCategory, relativePath);

        if (saved) {
            req.getSession().setAttribute("flashMessage", "Resource uploaded successfully. It is now pending review.");
            resp.sendRedirect(req.getContextPath() + "/student/dashboard");
        } else {
            new File(savedPath).delete();
            req.setAttribute("error", "Upload failed. Please try again.");
            repopulateForm(req, title, courseCode, description, resourceType, topicCategory);
            req.getRequestDispatcher("/WEB-INF/views/upload-resource.jsp").forward(req, resp);
        }
    }

    private void prepareUploadView(HttpServletRequest req, User user) {
        String contextPath = req.getContextPath();
        Map<String, Object> profile = dashboardService.getProfile(user.getUserId(), contextPath);
        if (profile.isEmpty()) {
            profile = Map.of(
                    "name", user.getFullName() != null ? user.getFullName() : "Student",
                    "initials", user.getInitials(),
                    "avatarUrl", "",
                    "reputationScore", dashboardService.getSummaryCards(user.getUserId()).getOrDefault("reputationScore", 0)
            );
        }

        Map<String, Object> summary = dashboardService.getSummaryCards(user.getUserId());
        req.setAttribute("profile", profile);
        req.setAttribute("topics", resourceService.getTopicsForUpload());
        req.setAttribute("impact", resourceService.getUploadImpactStats());
        req.setAttribute("activePage", "upload");
        req.setAttribute("searchQuery", "");
    }

    private void repopulateForm(HttpServletRequest req, String title, String courseCode,
                                String description, String resourceType, String topicCategory) {
        req.setAttribute("formTitle", title);
        req.setAttribute("formCourseCode", courseCode);
        req.setAttribute("formDescription", description);
        req.setAttribute("formResourceType", resourceType);
        req.setAttribute("formTopicCategory", topicCategory);
    }
}
