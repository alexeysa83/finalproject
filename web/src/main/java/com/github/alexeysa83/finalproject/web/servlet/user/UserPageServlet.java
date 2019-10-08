package com.github.alexeysa83.finalproject.web.servlet.user;

import com.github.alexeysa83.finalproject.service.auth.SecurityService;
import com.github.alexeysa83.finalproject.service.auth.DefaultSecurityService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebServlet(name = "UserPageServlet", urlPatterns = {"/userpage"})
public class UserPageServlet extends HttpServlet {

    private SecurityService securityService = DefaultSecurityService.getInstance();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
        String login = req.getParameter("userPage");
        // user attributes
        boolean f = securityService.checkLoginIsTaken(login);
        req.setAttribute("userPage", login);
        forwardToJsp("userpage", req, resp);
    }
}
