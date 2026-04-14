package com.ScholarShare.controller;

import com.ScholarShare.entity.User;
import com.ScholarShare.service.AuthService;
import com.ScholarShare.util.CookieUtil;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService = new AuthService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email=req.getParameter("email");
        String password = req.getParameter("password");


        User user = authService.login(email,password);

        if (user == null) {
            String error = authService.getLoginErrorMessage(email, password);
            req.setAttribute("error", error);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }else {
            System.out.println("login ok");
            SessionUtil.setAttribute(req, "user",user);
            CookieUtil.addCookie(resp, "email", user.getEmail(), 24 * 60 * 60);
            resp.sendRedirect(req.getContextPath() +"/home");
        }

    }
}


