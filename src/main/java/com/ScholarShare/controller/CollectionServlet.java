package com.ScholarShare.controller;

import com.ScholarShare.dao.daoImpl.CollectionDaoImpl;
import com.ScholarShare.entity.User;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/collections")
public class CollectionServlet extends HttpServlet {
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

        request.getRequestDispatcher("/WEB-INF/views/collections.jsp").forward(request, response);
    }
}
