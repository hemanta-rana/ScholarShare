package com.ScholarShare.controller;


import com.ScholarShare.service.AuthService;
import com.ScholarShare.dao.UserDao;
import com.ScholarShare.dao.daoImpl.UserDaoImpl;
import com.ScholarShare.entity.User;
import com.ScholarShare.util.PasswordUtil;
import com.ScholarShare.util.ValidationUtil;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/register")
public class SignUpServlet extends HttpServlet {
     private final UserDao userDao = new UserDaoImpl();
     private final AuthService authService = new AuthService();


    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String confirmPassword = req.getParameter("confirmPassword");
        Boolean pledgeAgreed = Boolean.valueOf(req.getParameter("pledgeAgreed"));
        String error = authService.register(fullName, email, phone, password, confirmPassword, pledgeAgreed);
        if (error == null) {
            req.setAttribute("SuccessMessage", "Registration successful!");
            req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
        } else {
            req.setAttribute("errorMessage", error);

            req.setAttribute("email", email);
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
        }
    }
}