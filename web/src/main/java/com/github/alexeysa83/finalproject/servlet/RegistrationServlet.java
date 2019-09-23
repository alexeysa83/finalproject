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
Pattern for username-->
UID-->
Welcome jsp (create+successfull login)-->
Password to hash class-->
 */
@WebServlet(name = "CreateUserServlet", urlPatterns = {"/registration"})
public class RegistrationServlet extends HttpServlet {

    private SecurityService securityService = DefaultSecurityService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        forwardToJsp("registration", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        // One class/method in service?
        String login = req.getParameter("login");
        if (login.length() < 1 || securityService.checkLoginIsTaken(login)) {
            req.setAttribute("error", "Login is already taken");
            doGet(req, resp);
        }

        String password = req.getParameter("password");
        String repeatpassword = req.getParameter("repeatpassword");
        if (password.length() < 1 || !(password.equals(repeatpassword))) {
            req.setAttribute("error", "Password and repeat password should be the same");
            doGet(req, resp);
        }

        AuthUser authUser = securityService.createAndSaveAuthUser(login, password, false);
        if (authUser == null) {
            req.setAttribute("error", "Unknown registration error");
            doGet(req, resp);
        }
        req.getSession().setAttribute("authUser", authUser);
        try {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
