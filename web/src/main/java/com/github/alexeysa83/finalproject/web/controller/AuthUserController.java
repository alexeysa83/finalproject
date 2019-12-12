package com.github.alexeysa83.finalproject.web.controller;

import com.github.alexeysa83.finalproject.model.Role;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.service.auth.AuthUserService;
import com.github.alexeysa83.finalproject.service.validation.AuthValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping(value = "/auth_users")
public class AuthUserController {

    private static final Logger log = LoggerFactory.getLogger(AuthUserController.class);

    private final AuthUserService authUserService;

    private final AuthValidationService validationService;

    public AuthUserController(AuthUserService authUserService, AuthValidationService validationService) {
        this.authUserService = authUserService;
        this.validationService = validationService;
    }

    @GetMapping("")
    public String forwardToRegistrationJSP() {
        return "registration";
    }

//    "/registration" POST
    @PostMapping(value = "")
    public String addUser(HttpServletRequest req) {

        final String login = req.getParameter("login");
        String message = validationService.isLoginValid(login);
        if (message != null) {
            req.setAttribute("message", message);
            return "registration";
        }
        // Optimize
        final String password = req.getParameter("password");
        final String passwordRepeat = req.getParameter("passwordRepeat");
        message = validationService.isPasswordValid(password,
                passwordRepeat);
        if (message != null) {
            req.setAttribute("message", message);
            return "registration";
        }

        final AuthUserDto authUser = authUserService.createAuthUserAndUserInfo(login, password);
        if (authUser == null) {
            req.setAttribute("message", "error.unknown");
            log.error("Failed to registrate user with login: {} pass {}, at: {}", login, password, LocalDateTime.now());
            return "registration";
        }

        req.getSession().setAttribute("authUser", authUser);
        log.info("User: {} registered at: {}", login, LocalDateTime.now());
        return "redirect:/index.jsp";
    }

//    "/auth/user/login" POST
    @PostMapping(value = "/{authId}/update_login")
    public String updateAuthUserLogin(HttpServletRequest req, @PathVariable Long authId) {
        final String loginNew = req.getParameter("login");
        String message = validationService.isLoginValid(loginNew);
        if (message != null) {
            req.setAttribute("message", message);
            return "forward:/user_infos/" + authId;
        }

        final AuthUserDto userOld = authUserService.getById(authId);

        final boolean isUpdated = authUserService.updateAuthUser
                (new AuthUserDto(userOld.getId(), loginNew, userOld.getPassword(),
                        userOld.getRole(), userOld.isDeleted(), userOld.getUserInfoDto()));
        message = "update.success";
        if (!isUpdated) {
            message = "update.fail";
            log.error("Failed to update login for user id: {}, at: {}", authId,  LocalDateTime.now());
            req.setAttribute("message", message);
            return "forward:/user_infos/" + authId;
        }
        log.info("Updated login for user id: {}, at: {}", authId,  LocalDateTime.now());
        req.setAttribute("message", message);
        return "forward:/logout";
    }

//    "/auth/user/password" POST
    @PostMapping(value = "/{authId}/update_password")
    public String updateAuthUserPassword(HttpServletRequest req, @PathVariable Long authId) {

        final String passwordNew = req.getParameter("passwordNew");
        final String passwordRepeat = req.getParameter("passwordRepeat");

        String message = validationService.isPasswordValid(passwordNew, passwordRepeat);
        if (message != null) {
            req.setAttribute("message", message);
            return "forward:/user_infos/" + authId;
        }

        final String passwordBefore = req.getParameter("passwordBefore");
        final AuthUserDto user = authUserService.getById(authId);
        final boolean isValid = validationService.isPasswordEqual(passwordBefore, user.getPassword());
        if (!isValid) {
            message = "wrong.pass";
            log.info("Invalid password enter for user id: {} at: {}", authId, LocalDateTime.now());
            req.setAttribute("message", message);
            return "forward:/user_infos/" + authId;
        }

        final boolean isUpdated = authUserService.updateAuthUser
                (new AuthUserDto(user.getId(), user.getLogin(), passwordNew,
                        user.getRole(), user.isDeleted(), user.getUserInfoDto()));
        message = "update.success";
        if (!isUpdated) {
            message = "update.fail";
            log.error("Failed to update password for user id: {} , at: {}", authId, LocalDateTime.now());
            req.setAttribute("message", message);
            return "forward:/user_infos/" + authId;
        }
        log.info("Updated password for user id: {}, at: {}", authId,  LocalDateTime.now());
        req.setAttribute("message", message);
        return "forward:/logout";
    }

//    "/admin/update/role" POST
    @PostMapping(value = "/{authId}/update_role")
    public String updateAuthUserRole(HttpServletRequest req, @PathVariable Long authId) {
        final String r = req.getParameter("role");
        boolean isRoleValid = validationService.isRoleValid(r);
        String message;
        if (!isRoleValid) {
            message = "update.fail";
            log.error("Failed to update role to user id: {} , at: {}", authId, LocalDateTime.now());
            req.setAttribute("message", message);
            return "forward:/user_infos/" + authId;
        }

        final Role role = Role.valueOf(r);
        final AuthUserDto user = authUserService.getById(authId);
        final boolean isUpdated = authUserService.updateAuthUser
                (new AuthUserDto(user.getId(), user.getLogin(),
                        user.getPassword(), role, user.isDeleted(), user.getUserInfoDto()));
        message = "update.success";
        if (!isUpdated) {
            message = "update.fail";
            log.error("Failed to update role to user id: {} , at: {}", authId, LocalDateTime.now());
            req.setAttribute("message", message);
            return "forward:/user_infos/" + authId;
        }
        log.info("Updated role to user id: {} , at: {}", authId, LocalDateTime.now());
        final AuthUserDto authUser = (AuthUserDto) req.getSession().getAttribute("authUser");
        final boolean needLogout = validationService.needLogout(authUser, authId);
        if (needLogout) {
            req.setAttribute("message", message);
            return "forward:/logout";
        }
        req.setAttribute("message", message);
        return "forward:/user_infos/" + authId;
    }

    // unsuccessful delete
//    "/auth/user/delete" GET
        @PostMapping(value = "/{authId}/delete")
    public String deleteAuthUser(HttpServletRequest req, @PathVariable Long authId) {
        final boolean isDeleted = authUserService.deleteUser(authId);
        String logMessage = "User id: {} deleted at: {}";
        String message = "delete.success";
        if (!isDeleted) {
            message = "delete.fail";
            logMessage = "Failed to delete user id: {} , at: {}";
            log.info(logMessage, authId, LocalDateTime.now());
            req.setAttribute("message", message);
            return "forward:/news";
        }
        log.info(logMessage, authId, LocalDateTime.now());

        final AuthUserDto authUser = (AuthUserDto) req.getSession().getAttribute("authUser");
        final boolean needLogout = validationService.needLogout(authUser, authId);
        if (needLogout) {
            req.setAttribute("message", message);
            return "forward:/logout";
        }
            return "forward:/user_infos/" + authId;
    }
}
