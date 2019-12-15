package com.github.alexeysa83.finalproject.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter (filterName = "EncodeFilter", urlPatterns = "/*")
public class EncodeServlet implements Filter {

    private static final String UTF_8 = "UTF-8";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(UTF_8);
        servletResponse.setCharacterEncoding(UTF_8);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
