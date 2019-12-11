package com.github.alexeysa83.finalproject.web.servlet.auth.user;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.auth.AuthUserService;
import com.github.alexeysa83.finalproject.service.validation.AuthValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping
public class UpdatePasswordServlet {

    @Autowired
    private AuthUserService authUserService;

    private AuthValidationService validationService = new AuthValidationService();

    private static final Logger log = LoggerFactory.getLogger(UpdatePasswordServlet.class);

    @PostMapping("/auth/user/password")
    public String doPost(HttpServletRequest req) {

        final String passwordNew = req.getParameter("passwordNew");
        final String passwordRepeat = req.getParameter("passwordRepeat");

        String message = validationService.isPasswordValid(passwordNew, passwordRepeat);
        if (message != null) {
            req.setAttribute("message", message);
            return "forward:/auth/user/view";
        }

        final String passwordBefore = req.getParameter("passwordBefore");
        final String authId = req.getParameter("authId");
        final long id = UtilService.stringToLong(authId);
        final AuthUserDto user = authUserService.getById(id);
        final boolean isValid = validationService.isPasswordEqual(passwordBefore, user.getPassword());
        if (!isValid) {
            message = "wrong.pass";
            log.info("Invalid password enter for user id: {} at: {}", authId, LocalDateTime.now());
            req.setAttribute("message", message);
            return "forward:/auth/user/view";
        }

        final boolean isUpdated = authUserService.updateAuthUser
                (new AuthUserDto(user.getId(), user.getLogin(), passwordNew,
                        user.getRole(), user.isDeleted(), user.getUserInfoDto()));
        message = "update.success";
        if (!isUpdated) {
            message = "update.fail";
            log.error("Failed to update password for user id: {} , at: {}", authId, LocalDateTime.now());
            req.setAttribute("message", message);
            return "forward:/auth/user/view";
        }
        log.info("Updated password for user id: {}, at: {}", authId,  LocalDateTime.now());
        req.setAttribute("message", message);
        return "forward:/auth/logout";
    }
}
