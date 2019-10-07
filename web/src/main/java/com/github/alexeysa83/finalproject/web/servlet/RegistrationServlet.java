package com.github.alexeysa83.finalproject.web.servlet;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.service.SecurityService;
import com.github.alexeysa83.finalproject.service.impl.DefaultSecurityService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

/*
Pattern for username-->
UID-->
Welcome jsp (create+successfull login)-->
Password to hash class-->
 */
@WebServlet(name = "RegistrationServlet", urlPatterns = {"/registration"})
public class RegistrationServlet extends HttpServlet {

    private SecurityService securityService = DefaultSecurityService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        forwardToJsp("registration", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        // One class/method in Validation service?
        String login = req.getParameter("login");
        if (login.length() < 1 || securityService.checkLoginIsTaken(login)) {
            req.setAttribute("error", "Login is already taken");
            doGet(req, resp);
            return;
        }

        String password = req.getParameter("password");
        String repeatpassword = req.getParameter("repeatpassword");
        if (password.length() < 1 || !(password.equals(repeatpassword))) {
            req.setAttribute("error", "Password and repeat password should be the same");
            doGet(req, resp);
            return;
        }

        AuthUser authUser = securityService.createAndSaveAuthUser(new AuthUser(login, password));
        if (authUser == null) {
            req.setAttribute("error", "Unknown registration error");
            doGet(req, resp);
            return;
        }
        req.getSession().setAttribute("authUser", authUser);
        try {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
