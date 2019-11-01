package com.github.alexeysa83.finalproject.web.servlet.auth.user;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.badge.DefaultBadgeService;
import com.github.alexeysa83.finalproject.service.user.DefaultUserService;
import com.github.alexeysa83.finalproject.service.user.UserService;
import com.github.alexeysa83.finalproject.service.validation.AuthValidationService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Set;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;
import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJspMessage;

@WebServlet(name = "UserPageServlet", urlPatterns = {"/auth/user/view"})
public class UserPageServlet extends HttpServlet {

    private UserService userService = DefaultUserService.getInstance();
    private BadgeService badgeService = DefaultBadgeService.getInstance();
    private AuthValidationService validationService = new AuthValidationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String authId = req.getParameter("authId");
        final long id = UtilService.stringToLong(authId);
        final UserInfoDto user = userService.getById(id);
        if (user == null) {
            String message = "deleted";
            forwardToJspMessage("userpage", message, req, resp);
        }
        req.setAttribute("userBadges", user.getBadges());
        req.setAttribute("user", user);

        AuthUserDto authUserDto = (AuthUserDto) req.getSession().getAttribute("authUser");
        boolean isAdmin = validationService.isAdmin(authUserDto.getRole());
        Set<BadgeDto> badgesDB = null;
        if (isAdmin) {
            badgesDB = badgeService.getAllBadges();
        }
        req.setAttribute("badgesDB", badgesDB);
        forwardToJsp("userpage", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }
}
