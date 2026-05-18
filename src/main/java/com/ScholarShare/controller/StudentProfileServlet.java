package com.ScholarShare.controller;

import com.ScholarShare.dao.daoImpl.StudentDaoImpl;
import com.ScholarShare.entity.User;
import com.ScholarShare.service.StudentDashboardService;
import com.ScholarShare.util.FileUploadUtil;
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
import java.util.Map;

@WebServlet("/student/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize       = 5  * 1024 * 1024,
        maxRequestSize    = 10 * 1024 * 1024
)
public class StudentProfileServlet extends HttpServlet {

    private final StudentDashboardService dashboardService = new StudentDashboardService();
    private final StudentDaoImpl studentDao = new StudentDaoImpl();

    /* ── GET: show profile page ── */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) SessionUtil.getAttribute(req, "user");
        if (user == null || !user.isStudent()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        loadProfileAttributes(req, user);
        req.getRequestDispatcher("/WEB-INF/views/user-profile-details.jsp").forward(req, resp);
    }

    /* ── POST: handle profile update or photo upload ── */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) SessionUtil.getAttribute(req, "user");
        if (user == null || !user.isStudent()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        HttpSession session = req.getSession(false);
        String action = req.getParameter("action");

        if ("updateInfo".equals(action)) {
            handleUpdateInfo(req, resp, session, user);
        } else if ("uploadPhoto".equals(action)) {
            handleUploadPhoto(req, resp, session, user);
        } else {
            resp.sendRedirect(req.getContextPath() + "/student/profile");
        }
    }

    /* ── Update name + phone ── */
    private void handleUpdateInfo(HttpServletRequest req, HttpServletResponse resp,
                                  HttpSession session, User user) throws IOException {
        String fullName = req.getParameter("fullName");
        String phone    = req.getParameter("phone");

        if (fullName == null || fullName.trim().isEmpty()) {
            session.setAttribute("flashError", "Full name cannot be empty.");
            resp.sendRedirect(req.getContextPath() + "/student/profile");
            return;
        }

        boolean ok = studentDao.updateProfile(user.getUserId(), fullName.trim(),
                phone != null ? phone.trim() : "");

        if (ok) {
            /* Refresh the user object in session so the header name updates immediately */
            user.setFullName(fullName.trim());
            if (phone != null) user.setPhone(phone.trim());
            session.setAttribute("user", user);
            session.setAttribute("flashMessage", "Profile updated successfully.");
        } else {
            session.setAttribute("flashError", "Could not update profile. Please try again.");
        }
        resp.sendRedirect(req.getContextPath() + "/student/profile");
    }

    /* ── Upload profile photo ── */
    private void handleUploadPhoto(HttpServletRequest req, HttpServletResponse resp,
                                   HttpSession session, User user) throws IOException, ServletException {
        int userId = user.getUserId();

        Part filePart = req.getPart("profilePic");
        if (filePart == null || filePart.getSize() == 0) {
            session.setAttribute("flashError", "Please select an image file.");
            resp.sendRedirect(req.getContextPath() + "/student/profile");
            return;
        }

        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            session.setAttribute("flashError", "Only image files (JPG, PNG, GIF, WebP) are allowed.");
            resp.sendRedirect(req.getContextPath() + "/student/profile");
            return;
        }

        String originalName  = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        // Sanitise filename — keep only safe characters
        String safeName      = originalName.replaceAll("[^a-zA-Z0-9._-]", "_");
        String savedFileName = userId + "_" + safeName;

        File uploadDir = FileUploadUtil.resolveUploadDir(getServletContext(), "uploads/profile_pics");

        try (InputStream in = filePart.getInputStream()) {
            Files.copy(in, Paths.get(uploadDir.getAbsolutePath() + File.separator + savedFileName),
                    StandardCopyOption.REPLACE_EXISTING);
        }

        String relativePath = "uploads/profile_pics/" + savedFileName.replace("\\", "/");
        boolean ok = studentDao.uploadProfilePicture(userId, relativePath);

        if (ok) {
            user.setProfilePic(relativePath);
            session.setAttribute("user", user);
            session.setAttribute("flashMessage", "Profile photo updated successfully.");
        } else {
            session.setAttribute("flashError", "Photo saved but database update failed. Please try again.");
        }
        resp.sendRedirect(req.getContextPath() + "/student/profile");
    }

    /* ── Shared attribute loader ── */
    private void loadProfileAttributes(HttpServletRequest req, User user) {
        Map<String, Object> profile = dashboardService.getProfile(user.getUserId(), req.getContextPath());
        Map<String, Object> summary = dashboardService.getSummaryCards(user.getUserId());
        Map<String, Object> reputation = dashboardService.getReputationBreakdown(user.getUserId());

        /* Expose raw user fields for the edit form */
        profile.put("phone", user.getPhone() != null ? user.getPhone() : "");

        req.setAttribute("profile",    profile);
        req.setAttribute("summary",    summary);
        req.setAttribute("reputation", reputation);
        req.setAttribute("searchQuery", "");

        /* Flash messages from session */
        HttpSession session = req.getSession(false);
        if (session != null) {
            Object flash = session.getAttribute("flashMessage");
            if (flash != null) { req.setAttribute("flashMessage", flash); session.removeAttribute("flashMessage"); }
            Object flashErr = session.getAttribute("flashError");
            if (flashErr != null) { req.setAttribute("flashError", flashErr); session.removeAttribute("flashError"); }
        }
    }
}
