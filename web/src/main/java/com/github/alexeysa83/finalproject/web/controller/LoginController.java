package com.github.alexeysa83.finalproject.web.controller;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.service.auth.AuthUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final AuthUserService authUserService;

    public LoginController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    //    "/login" GET
    @GetMapping
    public String loginGetMethod() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return "login";
        }
        return "redirect:/news";
    }

    //    "/login" POST
    @PostMapping
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

        final String role = userFromDB.getRole().toString();
        final Authentication authentication = new UsernamePasswordAuthenticationToken(userFromDB, null, getAuthorities(role));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User: {} logged in at: {}", login, LocalDateTime.now());
        return "redirect:/news";
    }

    private List<GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList((GrantedAuthority) () -> "ROLE_" + role);
    }
}
