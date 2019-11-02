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

import static com.github.alexeysa83.finalproject.web.WebUtils.*;

@WebServlet(name = "DeleteUserServlet", urlPatterns = {"/auth/user/delete"})
public class DeleteUserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DeleteUserServlet.class);
    private SecurityService securityService = DefaultSecurityService.getInstance();
    private AuthValidationService validationService = new AuthValidationService();

    // unsuccessful delete
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String authId = req.getParameter("authId");
        final long id = UtilService.stringToLong(authId);
        final boolean isDeleted = securityService.deleteUser(id);
        String logMessage = "User id: {} deleted at: {}";
        String message = "delete.success";
        if (!isDeleted) {
            message = "delete.fail";
            logMessage = "Failed to delete user id: {} , at: {}";
            log.info(logMessage, authId, LocalDateTime.now());
            forwardToJspMessage("index", message, req, resp);
        }
        log.info(logMessage, authId, LocalDateTime.now());

        final AuthUserDto authUser = (AuthUserDto) req.getSession().getAttribute("authUser");
        final boolean needLogout = validationService.needLogout(authUser, authId);
        if (needLogout) {
            forwardToServletMessage("/auth/logout", message, req, resp);
            return;
        }
        forwardToServletMessage("/auth/user/view", message, req, resp);
    }
}
