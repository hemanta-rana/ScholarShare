package com.ScholarShare.controller;

import com.ScholarShare.entity.User;
import com.ScholarShare.service.AuthService;
import com.ScholarShare.util.CookieUtil;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = authService.login(email, password);

        if (user == null) {
            String error = authService.getLoginErrorMessage(email, password);
            req.setAttribute("error", error);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        } else {
            SessionUtil.setAttribute(req, "user", user);
            CookieUtil.addCookie(resp, "email", user.getEmail(), 24 * 60 * 60);
            if (user.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else if (user.isStudent()) {
                resp.sendRedirect(req.getContextPath() + "/student/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        }
    }
}
