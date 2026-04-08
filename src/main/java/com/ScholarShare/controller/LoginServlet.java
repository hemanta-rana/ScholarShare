package com.ScholarShare.controller;

import com.ScholarShare.entity.User;
import com.ScholarShare.service.AuthService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService = new AuthService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email=req.getParameter("email");
        String password = req.getParameter("password");

        User user = authService.login(email,password);
if(user !=null){
    HttpSession session =req.getSession();
    session.setAttribute("user",user);
     if("admin".equals(user.getRole())) {
         resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
     }else{
         resp.sendRedirect(req.getContextPath()+"/student/dashboard");
     }
}else {
    String error = authService.getLoginErrorMessage(email, password);
    req.setAttribute("errorMessage", error);
    req.setAttribute("email", email);

    req.getRequestDispatcher("/Views/auth/login.jsp").forward(req, resp);
}
    }
}


