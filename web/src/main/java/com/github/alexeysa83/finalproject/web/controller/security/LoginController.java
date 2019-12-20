package com.github.alexeysa83.finalproject.web.controller.security;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.service.auth.AuthUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.Utils.WebAuthenticationUtils.setUserInSession;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final AuthUserService authUserService;

    public LoginController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @GetMapping
    public String loginGetMethod() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return "login_form";
        }
        return "redirect:/news/all";
    }

    @PostMapping
    public String loginPostMethod(@RequestParam(value = "login") String login,
                                  @RequestParam(value = "login") String password,
                                  ModelMap modelMap) {
        AuthUserDto userFromDB = authUserService.loginAuthUser(new AuthUserDto(login, password));
        if (userFromDB == null) {
            modelMap.addAttribute("message", "wrong.logpass");
            log.info("Invalid login or password enter for user: {} at: {}", login, LocalDateTime.now());
            return "login_form";
        }

        if (userFromDB.isDeleted()) {
            modelMap.addAttribute("message", "deleted");
            log.info("Deleted user: {} tried to login at: {}", login, LocalDateTime.now());
            return "login_form";
        }

        setUserInSession(userFromDB);
        log.info("User: {} logged in at: {}", login, LocalDateTime.now());
        return "redirect:/news/all";
    }


}
