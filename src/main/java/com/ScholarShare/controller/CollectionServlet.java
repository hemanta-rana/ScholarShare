package com.ScholarShare.controller;

import com.ScholarShare.dao.daoImpl.CollectionDaoImpl;
import com.ScholarShare.entity.User;
import com.ScholarShare.service.StudentDashboardService;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/collections")
public class CollectionServlet extends HttpServlet {

    private final StudentDashboardService dashboardService = new StudentDashboardService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        CollectionDaoImpl collectionDao = new CollectionDaoImpl();
        request.setAttribute("collections", collectionDao.getByUser(user.getUserId()));
        request.setAttribute("profile", dashboardService.getProfile(user.getUserId(), request.getContextPath()));

        request.getRequestDispatcher("/WEB-INF/views/collections.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getParameter("action");
        CollectionDaoImpl collectionDao = new CollectionDaoImpl();
        String redirectUrl = request.getContextPath() + "/collections";

        if ("create".equals(action)) {
            String collectionName = request.getParameter("collectionName");
            if (collectionName != null && !collectionName.trim().isEmpty()) {
                collectionDao.createCollection(user.getUserId(), collectionName.trim());
            }
        } else if ("add".equals(action)) {
            String collectionIdStr = request.getParameter("collectionId");
            String resourceIdStr   = request.getParameter("resourceId");
            if (collectionIdStr != null && resourceIdStr != null) {
                collectionDao.addResource(Integer.parseInt(collectionIdStr), Integer.parseInt(resourceIdStr));
                redirectUrl = request.getContextPath() + "/resource?id=" + resourceIdStr;
            }
        } else if ("remove".equals(action)) {
            String collectionIdStr = request.getParameter("collectionId");
            String resourceIdStr   = request.getParameter("resourceId");
            if (collectionIdStr != null && resourceIdStr != null) {
                collectionDao.removeResource(Integer.parseInt(collectionIdStr), Integer.parseInt(resourceIdStr));
            }
        } else if ("delete".equals(action)) {
            String collectionIdStr = request.getParameter("collectionId");
            if (collectionIdStr != null) {
                collectionDao.deleteCollection(Integer.parseInt(collectionIdStr));
            }
        }

        response.sendRedirect(redirectUrl);
    }
}
