package com.bank.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthenticationFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain)
            throws IOException, ServletException {

        String path = request.getServletPath();

        // Public resources
        if (path.equals("/")
                || path.equals("/login")
                || path.equals("/login.jsp")
                || path.equals("/403.jsp")
                || path.startsWith("/assets/")) {

            chain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {

            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

	response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        chain.doFilter(request, response);
    }
}
