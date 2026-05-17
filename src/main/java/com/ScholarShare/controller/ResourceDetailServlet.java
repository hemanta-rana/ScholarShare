package com.ScholarShare.controller;

import com.ScholarShare.entity.Resource;
import com.ScholarShare.service.ResourceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/resource-detail")
public class ResourceDetailServlet extends HttpServlet {

    private final ResourceService resourceService = new ResourceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int resourceId = Integer.parseInt(req.getParameter("id"));
        Resource resource = resourceService.getResourceById(resourceId);

        if (resource == null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        req.setAttribute("resource", resource);
        req.getRequestDispatcher("/WEB-INF/views/resource-detail.jsp").forward(req, resp);
    }
}
