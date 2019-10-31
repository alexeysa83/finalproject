package com.github.alexeysa83.finalproject.web.servlet;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
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
Divide login/password-->
Jsp for succesfull login+create-->
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(LoginServlet.class);
    private SecurityService securityService = DefaultSecurityService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Object authUser = req.getSession().getAttribute("authUser");
        if (authUser == null) {
            forwardToJsp("login", req, resp);
            return;
        }
        forwardToJsp("index", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String login = req.getParameter("login");
        final String password = req.getParameter("password");

        AuthUserDto userFromDB = securityService.login(new AuthUserDto(login, password, null));
        if (userFromDB == null) {
            req.setAttribute("message", "wrong.logpass");
            log.info("Invalid login or password enter for user: {} at: {}", login, LocalDateTime.now());
            forwardToJsp("login", req, resp);
            return;
        }
        // Translation
        if (userFromDB.isDeleted()) {
            req.setAttribute("message", "deleted");
            log.info("Deleted user: {} tried to login at: {}", login, LocalDateTime.now());
            forwardToJsp("login", req, resp);
            return;
        }
        req.getSession().setAttribute("authUser", userFromDB);
        log.info("User: {} logged in at: {}", login, LocalDateTime.now());
        redirect("/index.jsp", req, resp);
//        try {
//            resp.sendRedirect(req.getContextPath() + "/index.jsp");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
