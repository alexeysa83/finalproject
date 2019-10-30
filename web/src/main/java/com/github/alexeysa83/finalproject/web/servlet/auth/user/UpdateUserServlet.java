package com.github.alexeysa83.finalproject.web.servlet.auth.user;

import com.github.alexeysa83.finalproject.model.dto.UserDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.user.DefaultUserService;
import com.github.alexeysa83.finalproject.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.*;

@WebServlet(name = "UpdateUserServlet", urlPatterns = {"/auth/user/update"})
public class UpdateUserServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UpdateUserServlet.class);
    private UserService userService = DefaultUserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String authId = req.getParameter("authId");
        final long id = UtilService.stringToLong(authId);
        final UserDto user = userService.getById(id);
        if (user == null) {
            String message = "deleted";
            forwardToJspMessage("userpage", message, req, resp);
            return;
        }
        req.setAttribute("user", user);
        forwardToJsp("userupdate", req, resp);
    }

    // Validation + add null to DB instead of ""
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String id = req.getParameter("authId");
        final long authId = UtilService.stringToLong(id);
        final String firstName = req.getParameter("firstName");
        final String lastName = req.getParameter("lastName");
        final String email = req.getParameter("email");
        final String phone = req.getParameter("phone");
        final boolean isUpdated = userService.update(new UserDto(authId, firstName, lastName, email, phone));
        String message = "update.success";
        String logMessage = "Updated profile to user id: {} , at: {}";
        if (!isUpdated) {
            message = "update.fail";
            logMessage = "Failed to update profile to user id: {} , at: {}";
        }
        log.info(logMessage, id, LocalDateTime.now());
        forwardToServletMessage("/auth/user/view", message, req, resp);
    }
}
