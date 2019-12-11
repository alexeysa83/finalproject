package com.github.alexeysa83.finalproject.web.servlet.admin.update;

import com.github.alexeysa83.finalproject.model.Role;
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

// Optimization
@Controller
@RequestMapping
public class UpdateRoleServlet {

    @Autowired
    private AuthUserService authUserService;

    private AuthValidationService validationService = new AuthValidationService();

    private static final Logger log = LoggerFactory.getLogger(UpdateRoleServlet.class);

    @PostMapping("/admin/update/role")
    public String doPost(HttpServletRequest req) {
        final String authId = req.getParameter("authId");
        final long id = UtilService.stringToLong(authId);
        final String r = req.getParameter("role");
        boolean isRoleValid = validationService.isRoleValid(r);
        String message;
        if (!isRoleValid) {
            message = "update.fail";
            log.error("Failed to update role to user id: {} , at: {}", authId, LocalDateTime.now());
            req.setAttribute("message", message);
            return "forward:/auth/user/view";
        }

        final Role role = Role.valueOf(r);
        final AuthUserDto user = authUserService.getById(id);
        final boolean isUpdated = authUserService.updateAuthUser
                (new AuthUserDto(user.getId(), user.getLogin(),
                        user.getPassword(), role, user.isDeleted(), user.getUserInfoDto()));
        message = "update.success";
        if (!isUpdated) {
            message = "update.fail";
            log.error("Failed to update role to user id: {} , at: {}", authId, LocalDateTime.now());
            req.setAttribute("message", message);
            return "forward:/auth/user/view";
        }
        log.info("Updated role to user id: {} , at: {}", authId, LocalDateTime.now());
        final AuthUserDto authUser = (AuthUserDto) req.getSession().getAttribute("authUser");
        final boolean needLogout = validationService.needLogout(authUser, authId);
        if (needLogout) {
            req.setAttribute("message", message);
            return "forward:/auth/logout";
        }
        req.setAttribute("message", message);
        return "forward:/auth/user/view";
    }
}
