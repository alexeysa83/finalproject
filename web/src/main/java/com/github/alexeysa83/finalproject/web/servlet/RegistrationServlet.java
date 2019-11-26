package com.github.alexeysa83.finalproject.web.servlet;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;
import com.github.alexeysa83.finalproject.service.validation.AuthValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/*
Pattern for username-->
UID-->
Welcome jsp (create+successfull login)-->
Password to hash class-->
 */
@Controller
@RequestMapping
public class RegistrationServlet {

    private static final Logger log = LoggerFactory.getLogger(RegistrationServlet.class);
    @Autowired
    private SecurityService securityService;
    private AuthValidationService validationService = new AuthValidationService();

    @GetMapping("/registration")
    public String doGet() {
        return "registration";
    }

    @PostMapping("/registration")
    public String doPost(HttpServletRequest req) {

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

        final AuthUserDto authUser = securityService.createAuthUser(login, password);
        if (authUser == null) {
            req.setAttribute("message", "error.unknown");
            log.error("Failed to registrate user with login: {} pass {}, at: {}", login, password, LocalDateTime.now());
            return "registration";
        }

        req.getSession().setAttribute("authUser", authUser);
        log.info("User: {} registered at: {}", login, LocalDateTime.now());
        return "redirect:/index.jsp";
    }
}
