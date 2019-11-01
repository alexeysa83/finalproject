package com.github.alexeysa83.finalproject.web.servlet.admin.add;


import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.badge.DefaultBadgeService;
import com.github.alexeysa83.finalproject.service.user.DefaultUserService;
import com.github.alexeysa83.finalproject.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebServlet (name = "AddBadgeToUserServlet", urlPatterns = {"/admin/add/user_badge"})
public class AddBadgeToUserServlet extends HttpServlet {

    private UserService userService = DefaultUserService.getInstance();
    private BadgeService badgeService = DefaultBadgeService.getInstance();

    private static final Logger log = LoggerFactory.getLogger(AddBadgeToUserServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {

        final String id = req.getParameter("authId");
        final long authId = UtilService.stringToLong(id);
        final String id1 = req.getParameter("badgeId");
        final long badgeId = UtilService.stringToLong(id1);
        final UserInfoDto userInfoDto = userService.addBadgeToUser(authId, badgeId);
        final List<BadgeDto> badgesDB = badgeService.getAllBadges();
// == null
        req.setAttribute("user", userInfoDto);
        req.setAttribute("userBadges", userInfoDto.getBadges());
        req.setAttribute("badgesDB", badgesDB);
        forwardToJsp("userpage", req, resp);
    }
}
