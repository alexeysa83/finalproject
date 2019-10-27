package com.github.alexeysa83.finalproject.web.filter;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.service.validation.AuthValidationService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebFilter (filterName = "AdminFilter")
public class AdminFilter  implements Filter {

    private AuthValidationService validationService = new AuthValidationService();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        AuthUserDto authUserDto = (AuthUserDto) request.getSession().getAttribute("authUser");
        boolean isAdmin = validationService.isAdmin(authUserDto.getRole());
        if (!isAdmin) {
            request.setAttribute("message", "access.admin");
            forwardToJsp("index", request,response);
        } else {
            filterChain.doFilter(request,response);
        }
    }
}
