package com.github.alexeysa83.finalproject.web.filter;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.Role;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebFilter (filterName = "AdminFilter")
public class AdminFilter  implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        AuthUser authUser = (AuthUser) request.getSession().getAttribute("authUser");
        if (!authUser.getRole().equals(Role.ADMIN)) {
            request.setAttribute("error", "Access only for admin users");
            forwardToJsp("index", request,response);
        } else {
            filterChain.doFilter(request,response);
        }
    }
}
