package com.github.alexeysa83.finalproject.web.servlet.auth.user;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.auth.AuthUserService;
import com.github.alexeysa83.finalproject.service.validation.AuthValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping
public class DeleteUserServlet {

    @Autowired
    private AuthUserService authUserService;

    private AuthValidationService validationService = new AuthValidationService();

    private static final Logger log = LoggerFactory.getLogger(DeleteUserServlet.class);

    // unsuccessful delete
    @GetMapping("/auth/user/delete")
    public String doGet(HttpServletRequest req) {
        final String authId = req.getParameter("authId");
        final long id = UtilService.stringToLong(authId);
        final boolean isDeleted = authUserService.deleteUser(id);
        String logMessage = "User id: {} deleted at: {}";
        String message = "delete.success";
        if (!isDeleted) {
            message = "delete.fail";
            logMessage = "Failed to delete user id: {} , at: {}";
            log.info(logMessage, authId, LocalDateTime.now());
            req.setAttribute("message", message);
            return "index";
        }
        log.info(logMessage, authId, LocalDateTime.now());

        final AuthUserDto authUser = (AuthUserDto) req.getSession().getAttribute("authUser");
        final boolean needLogout = validationService.needLogout(authUser, authId);
        if (needLogout) {
            req.setAttribute("message", message);
            return "forward:/auth/logout";
        }
        return "forward:/auth/user/view";
    }
}
