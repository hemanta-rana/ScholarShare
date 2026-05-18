package com.ScholarShare.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * Serves uploaded files (profile pictures, resources) directly from the
 * persistent upload directory defined by the {@code uploadBasePath}
 * context-param in web.xml.
 *
 * <p>Maps {@code /uploads/*} so that URLs like
 * {@code /ScholarShare/uploads/profile_pics/1_photo.jpg} are resolved
 * against the source-tree webapp folder rather than the exploded WAR,
 * which means uploaded files survive Tomcat redeployments.</p>
 */
@WebServlet("/uploads/*")
public class UploadsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // pathInfo is e.g. "/profile_pics/1_photo.jpg"
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Resolve base directory from context-param (same as FileUploadUtil)
        String base = getServletContext().getInitParameter("uploadBasePath");
        if (base == null || base.isBlank()) {
            base = getServletContext().getRealPath("");
        }

        // Build the absolute file path — strip leading slash from pathInfo
        String relativePath = pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo;
        // Prevent path traversal attacks
        if (relativePath.contains("..")) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        File file = new File(base, "uploads" + File.separator
                + relativePath.replace("/", File.separator));

        if (!file.exists() || !file.isFile()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Detect content type
        String contentType = getServletContext().getMimeType(file.getName());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        resp.setContentType(contentType);
        resp.setContentLengthLong(file.length());

        // Cache images for 1 day
        if (contentType.startsWith("image/")) {
            resp.setHeader("Cache-Control", "public, max-age=86400");
        }

        try (OutputStream out = resp.getOutputStream()) {
            Files.copy(file.toPath(), out);
        }
    }
}
