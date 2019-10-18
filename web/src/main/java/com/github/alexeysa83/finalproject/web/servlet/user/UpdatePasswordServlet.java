package com.github.alexeysa83.finalproject.web.servlet.user;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.service.UtilsService;
import com.github.alexeysa83.finalproject.service.auth.DefaultSecurityService;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;
import com.github.alexeysa83.finalproject.service.validation.AuthValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServletMessage;

@WebServlet(name = "UpdatePasswordServlet", urlPatterns = {"/restricted/authuseruser/pass/update/password"})

public class UpdatePasswordServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UpdatePasswordServlet.class);
    private SecurityService securityService = DefaultSecurityService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        final String passwordNew = req.getParameter("passwordNew");
        final String passwordRepeat = req.getParameter("passwordRepeat");

        String message = AuthValidationService.isPasswordValid(passwordNew, passwordRepeat);
        if (message != null) {
            forwardToServletMessage("/restricted/user/profile", message, req, resp);
            return;
        }

        final String passwordBefore = req.getParameter("passwordBefore");
        final String authId = req.getParameter("authId");
        final long id = UtilsService.stringToLong(authId);
        final AuthUser user = securityService.getById(id);
        final boolean isValid = AuthValidationService.isPasswordEqual(passwordBefore, user.getPassword());
        if (!isValid) {
            message = "wrong.pass";
            log.info("Invalid password enter for user id: {} at: {}", authId, LocalDateTime.now());
            forwardToServletMessage("/restricted/user/profile", message, req, resp);
            return;
        }

        final boolean isUpdated = securityService.update
                (new AuthUser(user.getId(), user.getLogin(), passwordNew, user.getRole(), user.isBlocked()));
        message = "update.success";
        if (!isUpdated) {
            message = "update.fail";
            log.error("Failed to update password for user id: {} , at: {}", authId, LocalDateTime.now());
            forwardToServletMessage("/restricted/user/profile", message, req, resp);
        }
        log.info("Updated password for user id: {}, at: {}", authId,  LocalDateTime.now());
        forwardToServletMessage("/auth/logout", message, req, resp);
    }
}
