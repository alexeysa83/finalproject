package com.github.alexeysa83.finalproject.web.servlet.auth;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.User;
import com.github.alexeysa83.finalproject.service.ValidationService;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;
import com.github.alexeysa83.finalproject.service.auth.DefaultSecurityService;
import com.github.alexeysa83.finalproject.service.user.DefaultUserService;
import com.github.alexeysa83.finalproject.service.user.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;


import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

/*
Pattern for username-->
UID-->
Welcome jsp (create+successfull login)-->
Password to hash class-->
 */
@WebServlet(name = "RegistrationServlet", urlPatterns = {"/auth/registration"})
public class RegistrationServlet extends HttpServlet {

    private SecurityService securityService = DefaultSecurityService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        forwardToJsp("registration", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {


        final String login = req.getParameter("login");
        String message = ValidationService.isLoginValid(login);
        if (message != null) {
            req.setAttribute("message", message);
            doGet(req, resp);
            return;
        }
        // Optimize
        final String password = req.getParameter("password");
        final String passwordRepeat = req.getParameter("passwordRepeat");
        message = ValidationService.isPasswordValid(password,
                passwordRepeat);
        if (message != null) {
            req.setAttribute("message", message);
            doGet(req, resp);
            return;
        }

        final AuthUser authUser = securityService.createAndSaveAuthUser(new AuthUser(login, password));
        if (authUser == null ) {
            req.setAttribute("message", "Unknown registration error");
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
