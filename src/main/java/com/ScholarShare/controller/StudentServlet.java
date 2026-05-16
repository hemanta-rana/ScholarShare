package com.ScholarShare.controller;

import com.ScholarShare.dao.StudentDao;
import com.ScholarShare.dao.daoImpl.StudentDaoImpl;
import com.ScholarShare.entity.User;

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


// @author Janam Ale

@WebServlet("/student")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize       = 5 * 1024 * 1024,
        maxRequestSize    = 10 * 1024 * 1024
)
public class StudentServlet extends HttpServlet {

    // Upload folder relative to the web-app root
    private static final String UPLOAD_DIR = "uploads" + File.separator + "profile_pics";

    private StudentDao studentDao;


    @Override
    public void init() throws ServletException {
        studentDao = new StudentDaoImpl();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "dashboard";

        switch (action) {

            case "dashboard"  -> handleDashboard(req, resp, session);
            case "getScore"   -> handleGetScore(req, resp, session);
            case "getStatus"  -> handleGetStatus(req, resp, session);

            default -> {
                System.out.println("StudentServlet: unknown GET action — " + action);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
            }
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing action parameter");
            return;
        }

        switch (action) {

            case "agreeToIntegrityPledge" -> handleIntegrityPledge(req, resp, session);
            case "flagPlagiarism"         -> handleFlagPlagiarism(req, resp, session);
            case "uploadProfilePicture"   -> handleUploadProfilePicture(req, resp, session);
            case "likeResource"           -> handleLikeResource(req, resp, session);
            case "downloadResource"       -> handleDownloadResource(req, resp, session);

            default -> {
                System.out.println("StudentServlet: unknown POST action — " + action);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
            }
        }
    }


    //  GET handlers


    private void handleDashboard(HttpServletRequest req,
                                 HttpServletResponse resp,
                                 HttpSession session)
            throws ServletException, IOException {

        int userId = getSessionUserId(session);

        User student = studentDao.getUserById(userId);
        if (student == null) {
            System.out.println("StudentServlet: user not found for id " + userId);
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int    score  = studentDao.getContributorReputationScore(userId);
        String status = studentDao.getContributorStatus(userId);

        req.setAttribute("student", student);
        req.setAttribute("reputationScore",  score);
        req.setAttribute("contributorStatus", status);

        req.getRequestDispatcher("/WEB-INF/views/student-dashboard.jsp")
                .forward(req, resp);
    }


    private void handleGetScore(HttpServletRequest req,
                                HttpServletResponse resp,
                                HttpSession session)
            throws ServletException, IOException {

        int userId = getSessionUserId(session);
        int score  = studentDao.getContributorReputationScore(userId);

        req.setAttribute("reputationScore", score);
        req.getRequestDispatcher("/WEB-INF/views/student-dashboard.jsp")
                .forward(req, resp);
    }


    private void handleGetStatus(HttpServletRequest req,
                                 HttpServletResponse resp,
                                 HttpSession session)
            throws ServletException, IOException {

        int    userId = getSessionUserId(session);
        String status = studentDao.getContributorStatus(userId);

        req.setAttribute("contributorStatus", status);
        req.getRequestDispatcher("/WEB-INF/views/student-dashboard.jsp")
                .forward(req, resp);
    }


    //  POST handlers



    private void handleIntegrityPledge(HttpServletRequest req,
                                       HttpServletResponse resp,
                                       HttpSession session)
            throws IOException {

        int     userId  = getSessionUserId(session);
        boolean success = studentDao.agreeToIntegrityPledge(userId);

        if (success) {
            session.setAttribute("flashMessage", "Integrity pledge recorded successfully.");
            System.out.println("StudentServlet: integrity pledge signed by user id " + userId);
        } else {
            session.setAttribute("flashError", "Could not record pledge — you may have already signed it.");
            System.out.println("StudentServlet: integrity pledge failed for user id " + userId);
        }

        resp.sendRedirect(req.getContextPath() + "/student?action=dashboard");
    }


    private void handleFlagPlagiarism(HttpServletRequest req,
                                      HttpServletResponse resp,
                                      HttpSession session)
            throws IOException {

        int    userId     = getSessionUserId(session);
        String resourceIdParam = req.getParameter("resourceId");
        String reason     = req.getParameter("reason");

        if (resourceIdParam == null || reason == null || reason.trim().isEmpty()) {
            session.setAttribute("flashError", "Resource ID and reason are required to flag plagiarism.");
            resp.sendRedirect(req.getContextPath() + "/student?action=dashboard");
            return;
        }

        int resourceId;
        try {
            resourceId = Integer.parseInt(resourceIdParam);
        } catch (NumberFormatException e) {
            System.out.println("StudentServlet: invalid resourceId for flag — " + resourceIdParam);
            session.setAttribute("flashError", "Invalid resource ID.");
            resp.sendRedirect(req.getContextPath() + "/student?action=dashboard");
            return;
        }

        boolean success = studentDao.flagResourceForPlagiarism(resourceId, userId, reason.trim());

        if (success) {
            session.setAttribute("flashMessage", "Resource flagged for review. Thank you.");
            System.out.println("StudentServlet: resource " + resourceId + " flagged by user " + userId);
        } else {
            session.setAttribute("flashError", "Could not submit plagiarism flag. Please try again.");
            System.out.println("StudentServlet: flag failed — resource " + resourceId + " user " + userId);
        }

        resp.sendRedirect(req.getContextPath() + "/student?action=dashboard");
    }


    private void handleUploadProfilePicture(HttpServletRequest req,
                                            HttpServletResponse resp,
                                            HttpSession session)
            throws IOException, ServletException {

        int userId = getSessionUserId(session);

        Part filePart = req.getPart("profilePic");
        if (filePart == null || filePart.getSize() == 0) {
            session.setAttribute("flashError", "Please select a file to upload.");
            resp.sendRedirect(req.getContextPath() + "/student?action=dashboard");
            return;
        }

        // Validate content type
        String contentType = filePart.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            session.setAttribute("flashError", "Only image files are allowed.");
            resp.sendRedirect(req.getContextPath() + "/student?action=dashboard");
            return;
        }

        // Build save path: <webapp-root>/uploads/profile_pics/<userId>_<original-name>
        String originalName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String savedFileName = userId + "_" + originalName;

        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File   uploadDir  = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fullPath = uploadPath + File.separator + savedFileName;
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, Paths.get(fullPath), StandardCopyOption.REPLACE_EXISTING);
        }


        boolean success = studentDao.uploadProfilePicture(userId, userId);

        if (success) {
            // Keep the fresh path in session so JSP can update the avatar immediately
            session.setAttribute("profilePicPath", UPLOAD_DIR + "/" + savedFileName);
            session.setAttribute("flashMessage", "Profile picture updated successfully.");
            System.out.println("StudentServlet: profile picture uploaded for user id " + userId);
        } else {
            session.setAttribute("flashError", "File saved but DB update failed. Please try again.");
            System.out.println("StudentServlet: DB update failed for profile picture, user id " + userId);
        }

        resp.sendRedirect(req.getContextPath() + "/student?action=dashboard");
    }


    private void handleLikeResource(HttpServletRequest req,
                                    HttpServletResponse resp,
                                    HttpSession session)
            throws IOException {

        String contributorIdParam = req.getParameter("contributorId");

        if (contributorIdParam == null) {
            session.setAttribute("flashError", "Contributor ID is required.");
            resp.sendRedirect(req.getContextPath() + "/student?action=dashboard");
            return;
        }

        try {
            int contributorId = Integer.parseInt(contributorIdParam);
            boolean success   = studentDao.addReputationEventOnLike(contributorId);

            if (success) {
                session.setAttribute("flashMessage", "Resource liked!");
                System.out.println("StudentServlet: like event recorded for contributor " + contributorId);
            } else {
                session.setAttribute("flashError", "Could not record like. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("StudentServlet: invalid contributorId for like — " + contributorIdParam);
            session.setAttribute("flashError", "Invalid contributor ID.");
        }

        resp.sendRedirect(req.getContextPath() + "/student?action=dashboard");
    }


    private void handleDownloadResource(HttpServletRequest req,
                                        HttpServletResponse resp,
                                        HttpSession session)
            throws IOException {

        String contributorIdParam = req.getParameter("contributorId");

        if (contributorIdParam == null) {
            session.setAttribute("flashError", "Contributor ID is required.");
            resp.sendRedirect(req.getContextPath() + "/student?action=dashboard");
            return;
        }

        try {
            int contributorId = Integer.parseInt(contributorIdParam);
            boolean success   = studentDao.addReputationEventOnDownload(contributorId);

            if (success) {
                System.out.println("StudentServlet: download event recorded for contributor " + contributorId);
            } else {
                System.out.println("StudentServlet: download event failed for contributor " + contributorId);
            }
        } catch (NumberFormatException e) {
            System.out.println("StudentServlet: invalid contributorId for download — " + contributorIdParam);
        }

        // Redirect to the actual resource file (update path to match your resource serving URL)
        resp.sendRedirect(req.getContextPath() + "/student?action=dashboard");
    }


    //  Utility


    private int getSessionUserId(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user.getUserId();
    }
}
