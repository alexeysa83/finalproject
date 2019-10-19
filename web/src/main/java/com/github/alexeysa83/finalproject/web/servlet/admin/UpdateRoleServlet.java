package com.github.alexeysa83.finalproject.web.servlet.admin;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.Role;
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

// Optimization
@WebServlet(name = "UpdateRoleServlet", urlPatterns = {"/admin/update/role"})
public class UpdateRoleServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UpdateRoleServlet.class);
    private SecurityService securityService = DefaultSecurityService.getInstance();
    private AuthValidationService validationService = new AuthValidationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String authId = req.getParameter("authId");
        final long id = UtilsService.stringToLong(authId);
        final String r = req.getParameter("role");
        boolean isRoleValid = validationService.isRoleValid(r);
        String message;
        if (!isRoleValid) {
            message = "update.fail";
            log.error("Failed to update role to user id: {} , at: {}", authId, LocalDateTime.now());
            forwardToServletMessage("/auth/user/view", message, req, resp);
            return;
        }

        final Role role = Role.valueOf(r);
        final AuthUser user = securityService.getById(id);
        final boolean isUpdated = securityService.update
                (new AuthUser(user.getId(), user.getLogin(), user.getPassword(), role, user.isBlocked()));
        message = "update.success";
        if (!isUpdated) {
            message = "update.fail";
            log.error("Failed to update role to user id: {} , at: {}", authId, LocalDateTime.now());
            forwardToServletMessage("/auth/user/view", message, req, resp);
            return;
        }
        log.info("Updated role to user id: {} , at: {}", authId, LocalDateTime.now());
        final AuthUser authUser = (AuthUser) req.getSession().getAttribute("authUser");
        final boolean needLogout = validationService.needLogout(authUser, authId);
        if (needLogout) {
            forwardToServletMessage("/auth/logout", message, req, resp);
            return;
        }
        forwardToServletMessage("/auth/user/view", message, req, resp);
    }
}
