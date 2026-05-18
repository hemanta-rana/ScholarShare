package com.ScholarShare.controller;

import com.ScholarShare.dao.FlagDao;
import com.ScholarShare.dao.ResourceDao;
import com.ScholarShare.dao.daoImpl.FlagDaoImpl;
import com.ScholarShare.dao.daoImpl.ResourceDaoImpl;
import com.ScholarShare.entity.Flag;
import com.ScholarShare.entity.Resource;
import com.ScholarShare.entity.User;
import com.ScholarShare.util.SessionUtil;
import com.ScholarShare.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/student/flag-resource")
public class FlagServlet extends HttpServlet {

    private FlagDao flagDao = new FlagDaoImpl();
    private ResourceDao resourceDao = new ResourceDaoImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) SessionUtil.getAttribute(req, "user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String resourceIdParam = req.getParameter("resourceId");
        if (resourceIdParam == null || resourceIdParam.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        int resourceId;
        try {
            resourceId = Integer.parseInt(resourceIdParam.trim());
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        String reason = req.getParameter("reason");

        if (ValidationUtil.isNullOrEmpty(reason)) {
            req.getSession().setAttribute("flagError", "Reason is required.");
            resp.sendRedirect(req.getContextPath() + "/resource-detail?id=" + resourceId);
            return;
        }

        if (flagDao.hasStudentAlreadyFlagged(resourceId, user.getUserId())) {
            req.getSession().setAttribute("flagError", "You have already flagged this resource.");
            resp.sendRedirect(req.getContextPath() + "/resource-detail?id=" + resourceId);
            return;
        }

        Resource resource = resourceDao.getResourceById(resourceId);
        if (resource == null) {
            req.getSession().setAttribute("flagError", "Resource not found.");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        Flag flag = new Flag();
        flag.setResourceId(resourceId);
        flag.setFlaggedBy(user.getUserId());
        flag.setReason(reason);

        if (flagDao.createFlag(flag)) {
            req.getSession().setAttribute("flagSuccess", "Resource flagged successfully for review.");
        } else {
            req.getSession().setAttribute("flagError", "Failed to submit the flag.");
        }

        resp.sendRedirect(req.getContextPath() + "/resource-detail?id=" + resourceId);
    }
}