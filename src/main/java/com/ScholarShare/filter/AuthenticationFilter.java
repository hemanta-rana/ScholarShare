package com.ScholarShare.filter;

import com.ScholarShare.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response =  (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String path = uri.substring(contextPath.length());

        if (path.startsWith("/css") || path.startsWith("/js/") || path.startsWith("/images/")) { // later need to add the home page also
            filterChain.doFilter(request, response);
            return;
        }
        boolean isLoggedIn = SessionUtil.getAttribute(request, "user") != null;
        boolean isAuthPage = "/login".equals(path) || "/register".equals(path);

        if (!isLoggedIn && !isAuthPage) {
            response.sendRedirect(contextPath+"/login"); // TO DO change the path for user
            return;
        }
        if (isLoggedIn && isAuthPage) {
            response.sendRedirect(contextPath+"/home"); // TO DO change the path for user
            return;
        }
        filterChain.doFilter(request, response);

    }
}
