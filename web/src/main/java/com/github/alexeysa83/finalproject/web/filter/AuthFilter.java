package com.github.alexeysa83.finalproject.web.filter;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;
import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServletMessage;

@WebFilter(filterName = "AuthFilter")
public class AuthFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        AuthUserDto authUserDto = (AuthUserDto) request.getSession().getAttribute("authUser");
        if (authUserDto == null) {
            request.setAttribute("message", "access.auth");
            forwardToJsp("/WEB-INF/view/login", request, response);
        } else if (authUserDto.isDeleted()) {
            String message = "deleted";
            log.error("Deleted user id: {}, is logged in at: {}", authUserDto.getId(), LocalDateTime.now());
            forwardToServletMessage("/logout", message, request, response);
//            request.setAttribute("error", "User" + authUser.getLogin() + "is blocked");
//            String path = request.getContextPath() + "/logout";
//            request.getRequestDispatcher(path);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
