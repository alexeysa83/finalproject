package com.github.alexeysa83.finalproject.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "LocaleFilter")
public class LocaleFilter implements Filter {

    private final String DEFAULT_LOCALE = "en_US";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();

        final String locale = servletRequest.getParameter("locale");
        if (locale != null) {
            session.setAttribute("locale", locale);
        } else if ((session.getAttribute("locale")) == null) {
            session.setAttribute("locale", DEFAULT_LOCALE);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
