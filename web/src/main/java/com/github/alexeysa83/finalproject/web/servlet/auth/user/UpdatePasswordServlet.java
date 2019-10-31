package com.github.alexeysa83.finalproject.web.servlet.auth.user;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.service.UtilService;
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

@WebServlet(name = "UpdatePasswordServlet", urlPatterns = {"/auth/user/password"})

public class UpdatePasswordServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UpdatePasswordServlet.class);
    private SecurityService securityService = DefaultSecurityService.getInstance();
    private AuthValidationService validationService = new AuthValidationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        final String passwordNew = req.getParameter("passwordNew");
        final String passwordRepeat = req.getParameter("passwordRepeat");

        String message = validationService.isPasswordValid(passwordNew, passwordRepeat);
        if (message != null) {
            forwardToServletMessage("/auth/user/view", message, req, resp);
            return;
        }

        final String passwordBefore = req.getParameter("passwordBefore");
        final String authId = req.getParameter("authId");
        final long id = UtilService.stringToLong(authId);
        final AuthUserDto user = securityService.getById(id);
        final boolean isValid = validationService.isPasswordEqual(passwordBefore, user.getPassword());
        if (!isValid) {
            message = "wrong.pass";
            log.info("Invalid password enter for user id: {} at: {}", authId, LocalDateTime.now());
            forwardToServletMessage("/auth/user/view", message, req, resp);
            return;
        }

        final boolean isUpdated = securityService.update
                (new AuthUserDto(user.getId(), user.getLogin(), passwordNew,
                        user.getRole(), user.isBlocked(), user.getUserInfoDto()));
        message = "update.success";
        if (!isUpdated) {
            message = "update.fail";
            log.error("Failed to update password for user id: {} , at: {}", authId, LocalDateTime.now());
            forwardToServletMessage("/auth/user/view", message, req, resp);
        }
        log.info("Updated password for user id: {}, at: {}", authId,  LocalDateTime.now());
        forwardToServletMessage("/auth/logout", message, req, resp);
    }
}
