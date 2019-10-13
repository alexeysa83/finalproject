package com.github.alexeysa83.finalproject.web.servlet.user;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.service.validation.AuthValidationService;
import com.github.alexeysa83.finalproject.service.auth.DefaultSecurityService;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;
import com.github.alexeysa83.finalproject.web.servlet.auth.LoginServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServletMessage;

@WebServlet(name = "UpdateLoginServlet", urlPatterns = {"/restricted/authuseruser/update/login"})
public class UpdateLoginServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UpdateLoginServlet.class);
    private SecurityService securityService = DefaultSecurityService.getInstance();

    // optimaze
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String loginNew = req.getParameter("login");
        String message = AuthValidationService.isLoginValid(loginNew);
        if (message != null) {
            forwardToServletMessage("/restricted/user/profile", message, req, resp);
            return;
        }
        final String authId = req.getParameter("authId");
        final AuthUser userOld = securityService.getById(authId);

        final boolean isUpdated = securityService.update
                (new AuthUser(userOld.getId(), loginNew, userOld.getPassword(), userOld.getRole(), userOld.isBlocked()));
        message = "Update succesfull";
        if (!isUpdated) {
            message = "Update cancelled, please try again";
            log.error("Failed to update login for user id: {}, at: {}", authId,  LocalDateTime.now());
            forwardToServletMessage("/restricted/user/profile", message, req, resp);
        }
        log.info("Updated login for user id: {}, at: {}", authId,  LocalDateTime.now());
        forwardToServletMessage("/auth/logout", message, req, resp);
    }
}
