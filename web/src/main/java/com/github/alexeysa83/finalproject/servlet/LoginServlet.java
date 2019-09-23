package com.github.alexeysa83.finalproject.servlet;

import com.github.alexeysa83.finalproject.AuthUser;
import com.github.alexeysa83.finalproject.SecurityService;
import com.github.alexeysa83.finalproject.impl.DefaultSecurityService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.alexeysa83.finalproject.WebUtils.forwardToJsp;

/*
Divide login/password-->
Jsp for succesfull login+create-->
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private SecurityService securityService = DefaultSecurityService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Object authUser = req.getSession().getAttribute("authUser");
        if (authUser == null) {
            forwardToJsp("login", req, resp);
        }
        forwardToJsp("index", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        AuthUser authUser = securityService.login(login, password);
        if (authUser == null) {
            req.setAttribute("error", "Invalid login or password");
            forwardToJsp("login", req, resp);
        }
        req.getSession().setAttribute("authUser", authUser);
        try {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
