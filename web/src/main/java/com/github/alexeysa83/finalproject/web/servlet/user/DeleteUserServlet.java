package com.github.alexeysa83.finalproject.web.servlet.user;

import com.github.alexeysa83.finalproject.model.AuthUser;
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

@WebServlet(name = "DeleteUserServlet", urlPatterns = {"/restricted/authuser/delete"})
public class DeleteUserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DeleteUserServlet.class);
    private SecurityService securityService = DefaultSecurityService.getInstance();

    // unsuccessful delete
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String authId = req.getParameter("authId");
        final boolean isDeleted = securityService.delete(authId);
        log.info("User id: {} deleted at: {}", authId, LocalDateTime.now());
        final AuthUser authUser = (AuthUser) req.getSession().getAttribute("authUser");

        // With user name
//        final String login = req.getParameter("login");
        String message = "delete.success";
        final boolean needLogout = AuthValidationService.needLogout(authUser, authId);
        if (needLogout) {
            forwardToServletMessage("/auth/logout", message, req, resp);
            return;
        }
        forwardToServletMessage("/restricted/user/profile", message, req, resp);
    }
}
