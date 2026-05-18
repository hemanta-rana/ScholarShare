package com.ScholarShare.filter;

import com.ScholarShare.entity.User;
import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String contextPath = request.getContextPath();
        String path = request.getRequestURI().substring(contextPath.length());
        if (path.isEmpty()) {
            path = "/";
        }

        if (isPublicAsset(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isLoggedIn = SessionUtil.getAttribute(request, "user") != null;
        boolean isAuthPage = "/login".equals(path) || "/register".equals(path);
        User user = (User) SessionUtil.getAttribute(request, "user");

        if ("/home".equals(path) && isLoggedIn && user != null && user.isStudent()) {
            response.sendRedirect(contextPath + "/student/dashboard");
            return;
        }

        if (path.equals("/home") || path.startsWith("/home")) {
            filterChain.doFilter(request, response);
            return;
        }

        if ("/logout".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!isLoggedIn && !isAuthPage) {
            response.sendRedirect(contextPath + "/home");
            return;
        }

        if (isLoggedIn && user != null) {
            if (path.startsWith("/student") && !user.isStudent()) {
                response.sendRedirect(contextPath + (user.isAdmin() ? "/admin/dashboard" : "/home"));
                return;
            }
            if (path.startsWith("/admin") && !user.isAdmin()) {
                response.sendRedirect(contextPath + "/student/dashboard");
                return;
            }
        }

        if (isAuthPage && isLoggedIn && user != null) {
            if (user.isAdmin()) {
                response.sendRedirect(contextPath + "/admin/dashboard");
            } else {
                response.sendRedirect(contextPath + "/student/dashboard");
            }
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicAsset(String path) {
        return path.startsWith("/css")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/uploads/")
                || path.startsWith("/aboutUs");
    }
}
