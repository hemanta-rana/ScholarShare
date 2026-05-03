package com.ScholarShare.controller;

import com.ScholarShare.entity.User;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/adminDashboard")
public class AdminServlet extends HomeServlet{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = (User) SessionUtil.getAttribute(request, "user");


        request.getRequestDispatcher("WEB-INF/views/adminDashboard.jsp").forward(request, response);

    }


}
