package com.github.alexeysa83.finalproject.web.servlet.user;

import com.github.alexeysa83.finalproject.model.User;
import com.github.alexeysa83.finalproject.service.user.DefaultUserService;
import com.github.alexeysa83.finalproject.service.user.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;
import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJspMessage;

@WebServlet(name = "UserPageServlet", urlPatterns = {"/restricted/user/profile"})
public class UserPageServlet extends HttpServlet {

    private UserService userService = DefaultUserService.getInstance();

    // User deleted message in JSP for deleted users with LOGIN!!!
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String authId = req.getParameter("authId");
        final User user = userService.getById(authId);
        if (user == null) {
            String message = "blocked";
            forwardToJspMessage("userpage", message, req, resp);
        }
        req.setAttribute("user", user);
        forwardToJsp("userpage", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }
}
