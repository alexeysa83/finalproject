package com.github.alexeysa83.finalproject.web.servlet.auth;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;
import com.github.alexeysa83.finalproject.service.auth.DefaultSecurityService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

/*
Divide login/password-->
Jsp for succesfull login+create-->
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/auth/login"})
public class LoginServlet extends HttpServlet {

    private SecurityService securityService = DefaultSecurityService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Object authUser = req.getSession().getAttribute("authUser");
        if (authUser == null) {
            forwardToJsp("login", req, resp);
            return;
        }
        forwardToJsp("index", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String login = req.getParameter("login");
        final String password = req.getParameter("password");

        AuthUser userFromDB = securityService.login(new AuthUser(login, password));
        if (userFromDB == null) {
            req.setAttribute("message", "Invalid login or password");
            forwardToJsp("login", req, resp);
            return;
        }
        if (userFromDB.isBlocked()) {
            req.setAttribute("message", userFromDB.getLogin() + " is blocked");
            forwardToJsp("login", req, resp);
            return;
        }
        req.getSession().setAttribute("authUser", userFromDB);
        try {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
