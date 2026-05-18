package com.ScholarShare.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Adds security-related HTTP response headers to every response.
 */
@WebFilter("/*")
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;

        // Prevent MIME-type sniffing
        response.setHeader("X-Content-Type-Options", "nosniff");

        // Prevent clickjacking
        response.setHeader("X-Frame-Options", "SAMEORIGIN");

        // Basic XSS protection for older browsers
        response.setHeader("X-XSS-Protection", "1; mode=block");

        // Referrer policy
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        chain.doFilter(req, response);
    }
}
