package com.ScholarShare.controller;

import com.ScholarShare.dao.daoImpl.CollectionDaoImpl;
import com.ScholarShare.dao.daoImpl.ResourceDaoImpl;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.User;
import com.ScholarShare.service.StudentDashboardService;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/resource")
public class ResourceDetailServlet extends HttpServlet {

    private final StudentDashboardService dashboardService = new StudentDashboardService();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{

        String idStr = request.getParameter("id");
        if(idStr == null || idStr.isEmpty()){
            response.sendRedirect(request.getContextPath() + "/browser");
            return;
        }

        ResourceDaoImpl resourceDao = new ResourceDaoImpl();
        Resource resource = resourceDao.getById(Integer.parseInt(idStr));

        if(resource == null){
            response.sendRedirect(request.getContextPath() + "/browser");
            return;
        }
        request.setAttribute("resource", resource);

        User user = (User) SessionUtil.getAttribute(request, "user");
        if (user != null) {
            CollectionDaoImpl collectionDao = new CollectionDaoImpl();
            request.setAttribute("collections", collectionDao.getByUser(user.getUserId()));

            if (user.isStudent()) {
                // Student view — includes student sidebar, header, and profile modal
                request.setAttribute("profile", dashboardService.getProfile(user.getUserId(), request.getContextPath()));
                request.getRequestDispatcher("/WEB-INF/views/resource-detail.jsp").forward(request, response);
            } else {
                // Admin (or any non-student) view — uses admin layout, no student modal
                request.getRequestDispatcher("/WEB-INF/views/admin-resource-detail.jsp").forward(request, response);
            }
        } else {
            // Unauthenticated — send to student view (login guard will redirect)
            request.getRequestDispatcher("/WEB-INF/views/resource-detail.jsp").forward(request, response);
        }
    }
}
