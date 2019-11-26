package com.github.alexeysa83.finalproject.web.servlet;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;
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
Divide login/password-->
Jsp for succesfull login+create-->
 */
@Controller
@RequestMapping
public class LoginServlet {

    private static final Logger log = LoggerFactory.getLogger(LoginServlet.class);
    @Autowired
    private SecurityService securityService;

    @GetMapping("/login")
    public String doGet(HttpServletRequest req) {
        Object authUser = req.getSession().getAttribute("authUser");
        if (authUser == null) {
            return "login";
        }
        return "index";
    }

   @PostMapping("login")
    public String doPost(HttpServletRequest req) {
        final String login = req.getParameter("login");
        final String password = req.getParameter("password");

        AuthUserDto userFromDB = securityService.loginAuthUser(new AuthUserDto(login, password, null));
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
}
