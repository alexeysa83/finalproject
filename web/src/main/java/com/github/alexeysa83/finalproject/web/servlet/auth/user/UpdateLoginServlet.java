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

@WebServlet(name = "UpdateLoginServlet", urlPatterns = {"/auth/user/login"})
public class UpdateLoginServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UpdateLoginServlet.class);
    private SecurityService securityService = DefaultSecurityService.getInstance();
    private AuthValidationService validationService = new AuthValidationService();

    // optimize
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String loginNew = req.getParameter("login");
        String message = validationService.isLoginValid(loginNew);
        if (message != null) {
            forwardToServletMessage("/auth/user/view", message, req, resp);
            return;
        }
        final String authId = req.getParameter("authId");
        final long id = UtilService.stringToLong(authId);
        final AuthUserDto userOld = securityService.getById(id);

        final boolean isUpdated = securityService.update
                (new AuthUserDto(userOld.getId(), loginNew, userOld.getPassword(),
                        userOld.getRole(), userOld.isBlocked(), userOld.getUserDto()));
        message = "update.success";
        if (!isUpdated) {
            message = "update.fail";
            log.error("Failed to update login for user id: {}, at: {}", authId,  LocalDateTime.now());
            forwardToServletMessage("/auth/user/view", message, req, resp);
        }
        log.info("Updated login for user id: {}, at: {}", authId,  LocalDateTime.now());
        forwardToServletMessage("/auth/logout", message, req, resp);
    }
}
