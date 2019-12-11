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
public class UpdateLoginServlet {

    @Autowired
    private AuthUserService authUserService;

    private AuthValidationService validationService = new AuthValidationService();

    private static final Logger log = LoggerFactory.getLogger(UpdateLoginServlet.class);

    // optimize
    @PostMapping("/auth/user/login")
    public String doPost(HttpServletRequest req) {
        final String loginNew = req.getParameter("login");
        String message = validationService.isLoginValid(loginNew);
        if (message != null) {
            req.setAttribute("message", message);
            return "forward:/auth/user/view";
               }
        final String authId = req.getParameter("authId");
        final long id = UtilService.stringToLong(authId);
        final AuthUserDto userOld = authUserService.getById(id);

        final boolean isUpdated = authUserService.updateAuthUser
                (new AuthUserDto(userOld.getId(), loginNew, userOld.getPassword(),
                        userOld.getRole(), userOld.isDeleted(), userOld.getUserInfoDto()));
        message = "update.success";
        if (!isUpdated) {
            message = "update.fail";
            log.error("Failed to update login for user id: {}, at: {}", authId,  LocalDateTime.now());
            req.setAttribute("message", message);
            return "forward:/auth/user/view";
        }
        log.info("Updated login for user id: {}, at: {}", authId,  LocalDateTime.now());
        req.setAttribute("message", message);
        return "forward:/auth/logout";
    }
}
