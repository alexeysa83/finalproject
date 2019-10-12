package com.github.alexeysa83.finalproject.web.servlet.user;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.Role;
import com.github.alexeysa83.finalproject.service.validation.AuthValidationService;
import com.github.alexeysa83.finalproject.service.auth.DefaultSecurityService;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;
import com.github.alexeysa83.finalproject.web.servlet.auth.RegistrationServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServletMessage;

// Optimization
@WebServlet (name = "UpdateRoleServlet", urlPatterns = {"/restricted/authuseruser/update/role"})
public class UpdateRoleServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(RegistrationServlet.class);
    private SecurityService securityService = DefaultSecurityService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)  {
        final String r = req.getParameter("role");
        boolean isRoleValid = AuthValidationService.isRoleValid(r);
        String message = null;
        if (!isRoleValid) {
            message = "Update cancelled, please try again";
            //log.error("Failed to update role to user id: {} , at: {}", authId, LocalDateTime.now());
            forwardToServletMessage("/restricted/user/profile", message, req, resp);
            return;
        }

        final Role role = Role.valueOf(r);
        final String authId = req.getParameter("authId");
        final AuthUser user = securityService.getById(authId);
        final boolean isUpdated = securityService.update
                (new AuthUser(user.getId(), user.getLogin(), user.getPassword(), role, user.isBlocked()));
        message = "Update succesfull";
        if (!isUpdated) {
            message = "Update cancelled, please try again";
            log.error("Failed to update role to user id: {} , at: {}", authId, LocalDateTime.now());
            forwardToServletMessage("/restricted/user/profile", message, req, resp);
            return;
        }
        log.info("Updated role to user id: {} , at: {}", authId, LocalDateTime.now());
        final AuthUser authUser = (AuthUser) req.getSession().getAttribute("authUser");
        final boolean needLogout = AuthValidationService.needLogout(authUser, authId);
        if (needLogout) {
            forwardToServletMessage("/auth/logout", message, req, resp);
            return;
        }
        forwardToServletMessage("/restricted/user/profile", message, req, resp);
    }
}
