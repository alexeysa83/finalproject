package com.github.alexeysa83.finalproject.web.controller;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.user.UserService;
import com.github.alexeysa83.finalproject.service.validation.AuthValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping(value = "/user_infos")
public class UserInfoController {

    private static final Logger log = LoggerFactory.getLogger(UserInfoController.class);

    private final UserService userService;

    private final BadgeService badgeService;

    private final AuthValidationService validationService;

    public UserInfoController(UserService userService,
                              BadgeService badgeService,
                              AuthValidationService validationService) {
        this.userService = userService;
        this.badgeService = badgeService;
        this.validationService = validationService;
    }

    //    "/auth/user/view" GET
    @RequestMapping(value = "/{authId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String getUserInfoById(HttpServletRequest req, @PathVariable Long authId) {
        final UserInfoDto user = userService.getById(authId);
        if (user == null) {
            req.setAttribute("message", "deleted");
            return "userpage";
        }
        req.setAttribute("userBadges", user.getBadges());
        req.setAttribute("user", user);

        AuthUserDto authUserDto = (AuthUserDto) req.getSession().getAttribute("authUser");
        boolean isAdmin = validationService.isAdmin(authUserDto.getRole());
        if (isAdmin) {
            List<BadgeDto> badgesDB = badgeService.getAllBadges();
            req.setAttribute("badgesDB", badgesDB);
        }
        return "userpage";
    }

//    "/auth/user/update" GET
    @GetMapping(value = "/{authId}/torequest")
    public String setUserInfoToUpdate(HttpServletRequest req, @PathVariable Long authId) {
        final UserInfoDto user = userService.getById(authId);
        if (user == null) {
            req.setAttribute("message", "deleted");
            return "userpage";
        }
        req.setAttribute("user", user);
        return "userupdate";
    }

    // Validation + add null to DB instead of ""
//    "/auth/user/update" POST
    @PostMapping(value = "/{authId}/update")
    public String updateUserInfo(HttpServletRequest req, @PathVariable Long authId) {
        final String firstName = req.getParameter("firstName");
        final String lastName = req.getParameter("lastName");
        final String email = req.getParameter("email");
        final String phone = req.getParameter("phone");
        final boolean isUpdated = userService.updateUserInfo(new UserInfoDto(authId, firstName, lastName, email, phone));
        String message = "update.success";
        String logMessage = "Updated profile to user id: {} , at: {}";
        if (!isUpdated) {
            message = "update.fail";
            logMessage = "Failed to update profile to user id: {} , at: {}";
        }
        log.info(logMessage, authId, LocalDateTime.now());
        req.setAttribute("message", message);
        return "forward:/user_infos/" + authId;
    }

//    "/admin/add/user_badge" GET
    @GetMapping(value = "/{authId}/add/{badgeId}")
    public String addBadgeToUserInfo(HttpServletRequest req,
                                     @PathVariable Long authId,
                                     @PathVariable Long badgeId) {

        final UserInfoDto userInfoDto = userService.addBadgeToUser(authId, badgeId);
        final List<BadgeDto> badgesDB = badgeService.getAllBadges();
// == null
        req.setAttribute("user", userInfoDto);
        req.setAttribute("userBadges", userInfoDto.getBadges());
        req.setAttribute("badgesDB", badgesDB);
        return "userpage";
    }

    // Duplicate code
//    "/admin/delete/user_badge" GET
    @GetMapping(value = "/{authId}/delete/{badgeId}")
    public String deleteBadgeFromUserInfo(HttpServletRequest req,
                                          @PathVariable Long authId,
                                          @PathVariable Long badgeId) {
        final UserInfoDto userInfoDto = userService.deleteBadgeFromUser(authId, badgeId);
        final List<BadgeDto> badgesDB = badgeService.getAllBadges();
// == null
        req.setAttribute("user", userInfoDto);
        req.setAttribute("userBadges", userInfoDto.getBadges());
        req.setAttribute("badgesDB", badgesDB);
        return "userpage";
    }
}
