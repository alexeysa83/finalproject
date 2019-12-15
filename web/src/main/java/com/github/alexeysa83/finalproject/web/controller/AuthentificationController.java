package com.github.alexeysa83.finalproject.web.controller;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.service.auth.AuthUserService;
import com.github.alexeysa83.finalproject.web.WebUtils;
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
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
public class AuthentificationController {

    private static final Logger log = LoggerFactory.getLogger(AuthentificationController.class);

    private final AuthUserService authUserService;

    public AuthentificationController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    //    "/login" GET
    @GetMapping(value = "/login")
    public String loginGetMethod() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return "login";
        }
        return "redirect:/news";
    }

    //    "/login" POST
    @PostMapping(value = "/login")
    public String loginPostMethod(HttpServletRequest req, HttpSession session) {
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

//        req.getSession().setAttribute("authUser", userFromDB);
        session.setAttribute("userInSessionLogin", userFromDB.getLogin());
        session.setAttribute("userInSessionAuthId", userFromDB.getId());
        final String role = userFromDB.getRole().toString();
        final Authentication authentication = new UsernamePasswordAuthenticationToken(userFromDB, null, getAuthorities(role));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User: {} logged in at: {}", login, LocalDateTime.now());
        return "redirect:/news";
    }

    private List<GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList((GrantedAuthority) () -> "ROLE_" + role);
    }

    //    "/auth/logout" GET
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpServletRequest req) {
        final AuthUserDto principal = WebUtils.getUserInSession();
        SecurityContextHolder.clearContext();
        try {
            req.logout();
        } catch (ServletException e) {
            log.error("User id: {} unable to log out at: {}", principal.getId(), LocalDateTime.now());
            throw new RuntimeException();
        }
        log.info("User id: {} logged out at: {}", principal.getId(), LocalDateTime.now());

//        AuthUserDto authUser = (AuthUserDto) req.getSession().getAttribute("authUser");
//        req.getSession().removeAttribute("authUser");
//        req.getSession().invalidate();
//        log.info("User id: {} logged out at: {}", authUser.getId(), LocalDateTime.now());
        return "login";
    }
}
