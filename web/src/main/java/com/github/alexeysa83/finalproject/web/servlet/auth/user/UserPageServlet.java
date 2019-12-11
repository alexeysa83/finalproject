package com.github.alexeysa83.finalproject.web.servlet.auth.user;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.user.UserService;
import com.github.alexeysa83.finalproject.service.validation.AuthValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping
public class UserPageServlet {

    @Autowired
    private UserService userService;
    @Autowired
    private BadgeService badgeService;

    private AuthValidationService validationService = new AuthValidationService();

    @GetMapping("/auth/user/view")
    public String doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String authId = req.getParameter("authId");
        final long id = UtilService.stringToLong(authId);
        final UserInfoDto user = userService.getById(id);
        if (user == null) {
            req.setAttribute("message", "deleted");
            return "userpage";
        }
        req.setAttribute("userBadges", user.getBadges());
        req.setAttribute("user", user);

        AuthUserDto authUserDto = (AuthUserDto) req.getSession().getAttribute("authUser");
        boolean isAdmin = validationService.isAdmin(authUserDto.getRole());
        List<BadgeDto> badgesDB = null;
        if (isAdmin) {
            badgesDB = badgeService.getAllBadges();
        }
        req.setAttribute("badgesDB", badgesDB);
        return "userpage";
    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
//        doGet(req, resp);
//    }
}
