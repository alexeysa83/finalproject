package com.github.alexeysa83.finalproject.web.servlet.user;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.service.ValidationService;
import com.github.alexeysa83.finalproject.service.auth.DefaultSecurityService;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServlet;
import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServletMessage;

@WebServlet(name = "UpdatePasswordServlet", urlPatterns = {"/restricted/authuseruser/pass/update/password"})

public class UpdatePasswordServlet extends HttpServlet {

    private SecurityService securityService = DefaultSecurityService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        final String passwordNew = req.getParameter("passwordNew");
        final String passwordRepeat = req.getParameter("passwordRepeat");

        String message = ValidationService.isPasswordValid(passwordNew, passwordRepeat);
        if (message != null) {
            forwardToServletMessage("/restricted/user/profile", message, req, resp);
//                        req.setAttribute("message", message);
//            forwardToServlet("/restricted/user/profile", req, resp);
            return;
        }

        final String passwordOld = req.getParameter("passwordOld");
        final String authId = req.getParameter("authId");
//        final String login = req.getParameter("login");
        final AuthUser user = securityService.getById(authId);

        final boolean isValid = ValidationService.isPasswordEqual(passwordOld, user.getPassword());
        if (!isValid) {
            message = "Invalid password";
            forwardToServletMessage("/restricted/user/profile", message, req, resp);
            return;
        }

        final boolean isUpdated = securityService.update
                (new AuthUser(user.getId(), user.getLogin(), passwordNew, user.getRole(), user.isBlocked()));
        message = "Update succesfull";
        if (!isUpdated) {
            message = "Update cancelled, please try again";
            forwardToServletMessage("/restricted/user/profile", message, req, resp);
        }
        forwardToServletMessage("/auth/logout", message, req, resp);
    }
}
