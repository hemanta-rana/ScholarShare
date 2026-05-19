package com.ScholarShare.controller;

import com.ScholarShare.dao.daoImpl.CollectionDaoImpl;
import com.ScholarShare.dao.daoImpl.FlagDaoImpl;
import com.ScholarShare.dao.daoImpl.RatingDaoImpl;
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

@WebServlet({"/resource", "/resource-detail"})
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

        int resourceId;
        try {
            resourceId = Integer.parseInt(idStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/browser");
            return;
        }

        ResourceDaoImpl resourceDao = new ResourceDaoImpl();
        Resource resource = resourceDao.getById(resourceId);

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
                // Populate rating data for approved resources
                if (resource.isApproved()) {
                    RatingDaoImpl ratingDao = new RatingDaoImpl();
                    double avg = ratingDao.averageRating(resourceId);
                    boolean hasRated = ratingDao.hasUserRated(resourceId, user.getUserId());
                    request.setAttribute("averageRating", avg);
                    request.setAttribute("hasRated", hasRated);

                    // Populate flag data
                    FlagDaoImpl flagDao = new FlagDaoImpl();
                    boolean hasAlreadyFlagged = flagDao.hasStudentAlreadyFlagged(resourceId, user.getUserId());
                    request.setAttribute("hasAlreadyFlagged", hasAlreadyFlagged);
                }

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
