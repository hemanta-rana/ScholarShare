package com.ScholarShare.controller;

import com.ScholarShare.entity.User;
import com.ScholarShare.service.ResourceService;
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

@WebServlet("/student/upload-resource")
@MultipartConfig(maxFileSize = 1024 * 1024 * 50, maxRequestSize = 1024 * 1024 * 55)
public class ResourceUploadServlet extends HttpServlet {

    private final ResourceService resourceService = new ResourceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) SessionUtil.getAttribute(req, "user");
        if (user == null || !user.isStudent()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setAttribute("topics", resourceService.getTopicsForUpload());
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
        String description = req.getParameter("description");
        String resourceType = req.getParameter("resourceType");
        String topicCategory = req.getParameter("topicCategory");
        boolean pledgeAgreed = "yes".equals(req.getParameter("pledgeAgreed"));
        Part filePart = req.getPart("resourceFile");

        req.setAttribute("topics", resourceService.getTopicsForUpload());

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
            req.getRequestDispatcher("/WEB-INF/views/upload-resource.jsp").forward(req, resp);
            return;
        }

        String datePrefix = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = datePrefix + "_" + originalName.toLowerCase();
        String uploadPath = req.getServletContext().getRealPath("") + File.separator + "uploads" + File.separator + "resources";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String savedPath = uploadPath + File.separator + fileName;
        filePart.write(savedPath);

        String relativePath = "uploads/resources/" + fileName;
        boolean saved = resourceService.saveUploadedResource(user.getUserId(), title, description,
                resourceType, topicCategory, relativePath);

        if (saved) {
            resp.sendRedirect(req.getContextPath() + "/home?uploadSuccess=true");
        } else {
            new File(savedPath).delete();
            req.setAttribute("error", "Upload failed. Please try again.");
            req.getRequestDispatcher("/WEB-INF/views/upload-resource.jsp").forward(req, resp);
        }
    }
}
