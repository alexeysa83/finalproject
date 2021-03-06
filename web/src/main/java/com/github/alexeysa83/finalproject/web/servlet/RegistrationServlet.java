package com.github.alexeysa83.finalproject.web.servlet;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.service.validation.AuthValidationService;
import com.github.alexeysa83.finalproject.service.auth.DefaultSecurityService;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;
import static com.github.alexeysa83.finalproject.web.WebUtils.redirect;

/*
Pattern for username-->
UID-->
Welcome jsp (create+successfull login)-->
Password to hash class-->
 */
@WebServlet(name = "RegistrationServlet", urlPatterns = {"/registration"})
public class RegistrationServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(RegistrationServlet.class);
    private SecurityService securityService = DefaultSecurityService.getInstance();
    private AuthValidationService validationService = new AuthValidationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        forwardToJsp("registration", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        final String login = req.getParameter("login");
        String message = validationService.isLoginValid(login);
        if (message != null) {
            req.setAttribute("message", message);
            doGet(req, resp);
            return;
        }
        // Optimize
        final String password = req.getParameter("password");
        final String passwordRepeat = req.getParameter("passwordRepeat");
        message = validationService.isPasswordValid(password,
                passwordRepeat);
        if (message != null) {
            req.setAttribute("message", message);
            doGet(req, resp);
            return;
        }

        final AuthUserDto authUser = securityService.createAuthUser(login, password);
        if (authUser == null) {
            req.setAttribute("message", "error.unknown");
            log.error("Failed to registrate user with login: {} pass {}, at: {}", login, password, LocalDateTime.now());
            doGet(req, resp);
            return;
        }

        req.getSession().setAttribute("authUser", authUser);
        log.info("User: {} registered at: {}", login, LocalDateTime.now());
        redirect("/index.jsp", req, resp);
//        try {
//            resp.sendRedirect(req.getContextPath() + "/index.jsp");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
