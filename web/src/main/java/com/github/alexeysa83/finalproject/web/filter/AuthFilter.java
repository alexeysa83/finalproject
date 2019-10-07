package com.github.alexeysa83.finalproject.web.filter;

import com.github.alexeysa83.finalproject.model.AuthUser;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebFilter(filterName = "AuthFilter")
public class AuthFilter implements Filter {

    // Check block while using
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        AuthUser authUser = (AuthUser) request.getSession().getAttribute("authUser");
        if (authUser == null) {
            request.setAttribute("error", "Access only for authorized users");
            forwardToJsp("login", request, response);
        } else if (authUser.isBlocked()) {
            request.setAttribute("error", "User" + authUser.getLogin() + "is blocked");
            String path = request.getContextPath() + "/logout";
            request.getRequestDispatcher(path);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
