package com.ScholarShare.controller;

import com.ScholarShare.dao.daoImpl.AdminDaoImp;
import com.ScholarShare.entity.User;
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

@WebServlet("/admin/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize       = 5  * 1024 * 1024,
        maxRequestSize    = 10 * 1024 * 1024
)
public class AdminProfileServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads" + File.separator + "profile_pics";
    private final AdminDaoImp adminDao = new AdminDaoImp();

    /* ── GET: show profile page ── */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) SessionUtil.getAttribute(req, "user");
        if (user == null || !user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        loadFlashMessages(req);
        req.getRequestDispatcher("/WEB-INF/views/admin-profile.jsp").forward(req, resp);
    }

    /* ── POST: update info or upload photo ── */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) SessionUtil.getAttribute(req, "user");
        if (user == null || !user.isAdmin()) {
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
            resp.sendRedirect(req.getContextPath() + "/admin/profile");
        }
    }

    /* ── Update name + phone ── */
    private void handleUpdateInfo(HttpServletRequest req, HttpServletResponse resp,
                                  HttpSession session, User user) throws IOException {
        String fullName = req.getParameter("fullName");
        String phone    = req.getParameter("phone");

        if (fullName == null || fullName.trim().isEmpty()) {
            session.setAttribute("flashError", "Full name cannot be empty.");
            resp.sendRedirect(req.getContextPath() + "/admin/profile");
            return;
        }

        boolean ok = adminDao.updateAdminProfile(
                user.getUserId(),
                fullName.trim(),
                phone != null ? phone.trim() : ""
        );

        if (ok) {
            user.setFullName(fullName.trim());
            if (phone != null) user.setPhone(phone.trim());
            session.setAttribute("user", user);
            session.setAttribute("flashMessage", "Profile updated successfully.");
        } else {
            session.setAttribute("flashError", "Could not update profile. Please try again.");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/profile");
    }

    /* ── Upload profile photo ── */
    private void handleUploadPhoto(HttpServletRequest req, HttpServletResponse resp,
                                   HttpSession session, User user) throws IOException, ServletException {
        int userId = user.getUserId();

        Part filePart = req.getPart("profilePic");
        if (filePart == null || filePart.getSize() == 0) {
            session.setAttribute("flashError", "Please select an image file.");
            resp.sendRedirect(req.getContextPath() + "/admin/profile");
            return;
        }

        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            session.setAttribute("flashError", "Only image files (JPG, PNG, GIF, WebP) are allowed.");
            resp.sendRedirect(req.getContextPath() + "/admin/profile");
            return;
        }

        if (filePart.getSize() > 5 * 1024 * 1024) {
            session.setAttribute("flashError", "File is too large. Maximum size is 5 MB.");
            resp.sendRedirect(req.getContextPath() + "/admin/profile");
            return;
        }

        String originalName  = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        // Sanitise filename — keep only alphanumeric, dots, hyphens, underscores
        String safeName      = originalName.replaceAll("[^a-zA-Z0-9._-]", "_");
        String savedFileName = "admin_" + userId + "_" + safeName;

        File uploadDir = FileUploadUtil.resolveUploadDir(getServletContext(), "uploads/profile_pics");

        try (InputStream in = filePart.getInputStream()) {
            Files.copy(in, Paths.get(uploadDir.getAbsolutePath() + File.separator + savedFileName),
                    StandardCopyOption.REPLACE_EXISTING);
        }

        String relativePath = "uploads/profile_pics/" + savedFileName;
        boolean ok = adminDao.uploadAdminProfilePicture(userId, relativePath);

        if (ok) {
            user.setProfilePic(relativePath);
            session.setAttribute("user", user);
            session.setAttribute("flashMessage", "Profile photo updated successfully.");
        } else {
            session.setAttribute("flashError", "Photo saved but database update failed. Please try again.");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/profile");
    }

    private void loadFlashMessages(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return;
        Object flash = session.getAttribute("flashMessage");
        if (flash != null) { req.setAttribute("flashMessage", flash); session.removeAttribute("flashMessage"); }
        Object flashErr = session.getAttribute("flashError");
        if (flashErr != null) { req.setAttribute("flashError", flashErr); session.removeAttribute("flashError"); }
    }
}
