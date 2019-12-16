package com.github.alexeysa83.finalproject.web.controller;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.web.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    private static final Logger log = LoggerFactory.getLogger(LogoutController.class);

    //    "/auth/logout" GET
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpServletRequest req) {
        final AuthUserDto principal = WebUtils.getUserInSession();
        SecurityContextHolder.clearContext();
        try {
            req.logout();
        } catch (ServletException e) {
            log.error("User id: {} unable to log out at: {}", principal.getId(), LocalDateTime.now());
            throw new RuntimeException();
        }
        req.setAttribute("message", "logout.user");
        log.info("User id: {} logged out at: {}", principal.getId(), LocalDateTime.now());
        return "login";
    }
}
