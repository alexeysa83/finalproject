package com.github.alexeysa83.finalproject.web.servlet.auth.user;

import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Controller
@RequestMapping
public class UpdateUserServlet {

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(UpdateUserServlet.class);

    @GetMapping("/auth/user/update")
    public String doGet(HttpServletRequest req) {
        final String authId = req.getParameter("authId");
        final long id = UtilService.stringToLong(authId);
        final UserInfoDto user = userService.getById(id);
        if (user == null) {
            req.setAttribute("message", "deleted");
            return "userpage";
        }
        req.setAttribute("user", user);
        return "userupdate";
    }

    // Validation + add null to DB instead of ""
    @PostMapping("/auth/user/update")
    public String doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String id = req.getParameter("authId");
        final long authId = UtilService.stringToLong(id);
        final String firstName = req.getParameter("firstName");
        final String lastName = req.getParameter("lastName");
        final String email = req.getParameter("email");
        final String phone = req.getParameter("phone");
        final boolean isUpdated = userService.updateUserInfo(new UserInfoDto(authId, firstName, lastName, email, phone));
        String message = "update.success";
        String logMessage = "Updated profile to user id: {} , at: {}";
        if (!isUpdated) {
            message = "update.fail";
            logMessage = "Failed to update profile to user id: {} , at: {}";
        }
        log.info(logMessage, id, LocalDateTime.now());
        req.setAttribute("message", message);
        return "forward:/auth/user/view";
    }
}
