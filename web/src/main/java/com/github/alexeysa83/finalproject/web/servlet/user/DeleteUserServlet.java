package com.github.alexeysa83.finalproject.web.servlet.user;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.service.ValidationService;
import com.github.alexeysa83.finalproject.service.auth.DefaultSecurityService;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;
import com.github.alexeysa83.finalproject.service.user.DefaultUserService;
import com.github.alexeysa83.finalproject.service.user.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.alexeysa83.finalproject.web.WebUtils.*;

@WebServlet(name = "DeleteUser", urlPatterns = {"/restricted/authuser/delete"})
public class DeleteUserServlet extends HttpServlet {

    private SecurityService securityService = DefaultSecurityService.getInstance();
//    private UserService userService = DefaultUserService.getInstance();


    // transaction + unsuccesfull delete
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String authId = req.getParameter("authId");
        final boolean isDeleted = securityService.delete(authId);
//        userService.delete(authId);
        final AuthUser authUser = (AuthUser) req.getSession().getAttribute("authUser");
        final String login = req.getParameter("login");
        String message = login + " deleted successfully";
        final boolean needLogout = ValidationService.needLogout(authUser, authId);
        if (needLogout) {
            forwardToServletMessage("/auth/logout", message, req, resp);
            return;
        }
        forwardToServletMessage("/restricted/user/profile", message, req, resp);
    }
}
