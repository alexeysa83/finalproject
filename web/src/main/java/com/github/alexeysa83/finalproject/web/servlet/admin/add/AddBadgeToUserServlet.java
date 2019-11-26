package com.github.alexeysa83.finalproject.web.servlet.admin.add;


import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping
public class AddBadgeToUserServlet {

    @Autowired
    private UserService userService;
    @Autowired
    private BadgeService badgeService;

    private static final Logger log = LoggerFactory.getLogger(AddBadgeToUserServlet.class);

    @GetMapping("/admin/add/user_badge")
    public String doGet(HttpServletRequest req) {

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
        return "userpage";
    }
}
