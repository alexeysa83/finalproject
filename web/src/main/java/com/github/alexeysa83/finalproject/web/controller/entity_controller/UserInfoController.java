package com.github.alexeysa83.finalproject.web.controller.entity_controller;

import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.user.UserService;
import com.github.alexeysa83.finalproject.web.request_entity.UserInfoRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static com.github.alexeysa83.finalproject.web.Utils.AuthenticationUtils.getPrincipalUserAuthId;
import static com.github.alexeysa83.finalproject.web.Utils.AuthenticationUtils.isPrincipalUserAdmin;
import static com.github.alexeysa83.finalproject.web.Utils.MessageContainer.*;

@Controller
@RequestMapping(value = "/user_infos")
public class UserInfoController {

    private static final Logger log = LoggerFactory.getLogger(UserInfoController.class);

    private final UserService userService;

    private final BadgeService badgeService;

    public UserInfoController(UserService userService, BadgeService badgeService) {
        this.userService = userService;
        this.badgeService = badgeService;
    }

    @GetMapping(value = "/{authId}")
    public String getUserInfoById(@PathVariable Long authId,
                                  @RequestParam(name = "returnPage", defaultValue = "user_info") String returnPage,
                                  ModelMap modelMap) {
        final String DEFAULT_RETURN_PAGE_USER_INFO = "user_info";
        final String RETURN_PAGE_USER_UPDATE = "user_update";

        final UserInfoDto userFromDB = userService.getById(authId);
        if (userFromDB == null) {
            modelMap.addAttribute(ERROR_ATTRIBUTE, USER_DELETED);
            return DEFAULT_RETURN_PAGE_USER_INFO;
        }
        modelMap.addAttribute(USER_ATTRIBUTE, userFromDB);

        switch (returnPage) {
            case RETURN_PAGE_USER_UPDATE:
                return returnPage;
            case DEFAULT_RETURN_PAGE_USER_INFO:
                modelMap.addAttribute(USER_BADGES_ATTRIBUTE, userFromDB.getBadges());
                if (isPrincipalUserAdmin()) {
                    List<BadgeDto> badgesDB = badgeService.getAllBadges();
                    modelMap.addAttribute(BADGES_ALL_FROM_DB, badgesDB);
                }
                return returnPage;
            default:
                log.error("Unknown return page: {},  user id: {} , at: {}", returnPage, authId, LocalDateTime.now());
                throw new RuntimeException("Wrong return page variable");
        }
    }

    /**
     * Null to database instead of ""
     */
    @PostMapping(value = "/{userToUpdateId}/update")
    public String updateUserInfo(@PathVariable Long userToUpdateId,
                                 @Valid UserInfoRequestModel userFromUpdateForm,
                                 BindingResult br,
                                 RedirectAttributesModelMap redirectModelMap) {
        final String RETURN_PAGE_FAIL_UPDATE = "redirect:/user_infos/" + userToUpdateId + "?returnPage=user_update";
        final String RETURN_PAGE_SUCCESS_UPDATE = "redirect:/user_infos/" + userToUpdateId;

        if ((!getPrincipalUserAuthId().equals(userToUpdateId)) & !(isPrincipalUserAdmin())) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, NO_PERMISSION_TO_UPDATE);
            return RETURN_PAGE_FAIL_UPDATE;
        }

        if (br.hasFieldErrors()) {
            final String errorMessage = br.getFieldError().getDefaultMessage();
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, errorMessage);
            return RETURN_PAGE_FAIL_UPDATE;
        }

        final UserInfoDto userToUpdate = userFromUpdateForm.convertToUserInfoDto();
        userToUpdate.setAuthId(userToUpdateId);
        final boolean isUpdated = userService.updateUserInfo(userToUpdate);
        if (!isUpdated) {
            log.error("Failed to update profile to user id: {} , at: {}", userToUpdateId, LocalDateTime.now());
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, FAILED_TO_UPDATE);
            return RETURN_PAGE_FAIL_UPDATE;
        }

        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, SUCCESSFUL_UPDATE);
        log.info("Updated profile to user id: {} , at: {}", userToUpdateId, LocalDateTime.now());
        return RETURN_PAGE_SUCCESS_UPDATE;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{authId}/add/{badgeId}")
    public String addBadgeToUserInfo(@PathVariable Long authId,
                                     @PathVariable Long badgeId,
                                     RedirectAttributesModelMap redirectModelMap) {

        final String DEFAULT_RETURN_PAGE = "redirect:/user_infos/" + authId;
        final UserInfoDto userWithBadge = userService.addBadgeToUser(authId, badgeId);
        if (userWithBadge == null) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, FAILED_TO_UPDATE);
            log.error("Failed to add badge id: {} to user id: {} , at: {}", badgeId, authId, LocalDateTime.now());
            return DEFAULT_RETURN_PAGE;
        }
        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, SUCCESSFUL_UPDATE);
        return DEFAULT_RETURN_PAGE;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{authId}/delete/{badgeId}")
    public String deleteBadgeFromUserInfo(@PathVariable Long authId,
                                          @PathVariable Long badgeId,
                                          RedirectAttributesModelMap redirectModelMap) {

        final String DEFAULT_RETURN_PAGE = "redirect:/user_infos/" + authId;
        final UserInfoDto userDeletedBadge = userService.deleteBadgeFromUser(authId, badgeId);
        if (userDeletedBadge == null) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, FAILED_TO_DELETE);
            log.error("Failed to delete badge id: {} from user id: {} , at: {}", badgeId, authId, LocalDateTime.now());
            return DEFAULT_RETURN_PAGE;
        }
        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, SUCCESSFUL_DELETE);
        return DEFAULT_RETURN_PAGE;
    }
}
