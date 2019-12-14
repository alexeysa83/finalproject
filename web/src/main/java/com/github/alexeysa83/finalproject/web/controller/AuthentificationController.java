package com.github.alexeysa83.finalproject.web.controller;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.service.auth.AuthUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
public class AuthentificationController {

    private static final Logger log = LoggerFactory.getLogger(AuthentificationController.class);

    private final AuthUserService authUserService;

    public AuthentificationController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

//    "/login" GET
    @GetMapping(value = "/login")
    public String loginGetMethod(HttpServletRequest req) {
        Object authUser = req.getSession().getAttribute("authUser");
        if (authUser == null) {
            return "login";
        }
        return "redirect:/index.jsp";
    }

    //    "/login" POST
    @PostMapping(value = "/login")
    public String loginPostMethod(HttpServletRequest req) {
        final String login = req.getParameter("login");
        final String password = req.getParameter("password");

        AuthUserDto userFromDB = authUserService.loginAuthUser(new AuthUserDto(login, password));
        if (userFromDB == null) {
            req.setAttribute("message", "wrong.logpass");
            log.info("Invalid login or password enter for user: {} at: {}", login, LocalDateTime.now());
            return "login";
        }
        // Translation
        if (userFromDB.isDeleted()) {
            req.setAttribute("message", "deleted");
            log.info("Deleted user: {} tried to login at: {}", login, LocalDateTime.now());
            return "login";
        }
        req.getSession().setAttribute("authUser", userFromDB);
        log.info("User: {} logged in at: {}", login, LocalDateTime.now());
        return "redirect:/index.jsp";
    }

//    "/auth/logout" GET
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpServletRequest req) {
        AuthUserDto authUser = (AuthUserDto) req.getSession().getAttribute("authUser");
        req.getSession().removeAttribute("authUser");
        req.getSession().invalidate();
        log.info("User id: {} logged out at: {}", authUser.getId(), LocalDateTime.now());
        return "login";
    }
}