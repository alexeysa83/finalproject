package com.github.alexeysa83.finalproject.web.servlet.user;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.Role;
import com.github.alexeysa83.finalproject.service.ValidationService;
import com.github.alexeysa83.finalproject.service.auth.DefaultSecurityService;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServletMessage;

@WebServlet (name = "UpdateRoleServlet", urlPatterns = {"/restricted/authuseruser/update/role"})
public class UpdateRoleServlet extends HttpServlet {

    private SecurityService securityService = DefaultSecurityService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)  {
        final String r = req.getParameter("role");
        boolean isRoleValid = ValidationService.isRoleValid(r);
        String message = null;
        if (!isRoleValid) {
            message = "Update cancelled, please try again";
            forwardToServletMessage("/restricted/user/profile", message, req, resp);
        }

        final Role role = Role.valueOf(r);
        final String authId = req.getParameter("authId");
        final AuthUser user = securityService.getById(authId);
        final boolean isUpdated = securityService.update
                (new AuthUser(user.getId(), user.getLogin(), user.getPassword(), role, user.isBlocked()));
        message = "Update succesfull";
        if (!isUpdated) {
            message = "Update cancelled, please try again";
            forwardToServletMessage("/restricted/user/profile", message, req, resp);
        }

        final AuthUser authUser = (AuthUser) req.getSession().getAttribute("authUser");
        final boolean needLogout = ValidationService.needLogout(authUser, authId);
        if (needLogout) {
            forwardToServletMessage("/auth/logout", message, req, resp);
            return;
        }
        forwardToServletMessage("/restricted/user/profile", message, req, resp);
    }
}
