package com.bank.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = {
        "/users",
        "/reports"
})
public class AuthorizationFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String role = (String) session.getAttribute("role");

        if (!"ADMIN".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/403.jsp");
            return;
        }

        chain.doFilter(request, response);
    }
}
