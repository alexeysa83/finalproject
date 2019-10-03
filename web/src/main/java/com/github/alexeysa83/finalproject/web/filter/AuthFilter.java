package com.github.alexeysa83.finalproject.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebFilter(displayName = "AuthFilter", urlPatterns = {"/userpage"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Object authUser = request.getSession().getAttribute("authUser");
        if (authUser == null) {
            forwardToJsp("login", request, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
