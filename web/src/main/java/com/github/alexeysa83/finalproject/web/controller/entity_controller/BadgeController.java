package com.github.alexeysa83.finalproject.web.controller.entity_controller;


import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.web.request_entity.BadgeRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static com.github.alexeysa83.finalproject.web.Utils.MessageContainer.*;

@Controller
@RequestMapping(value = "/badges")
public class BadgeController {

    private static final Logger log = LoggerFactory.getLogger(BadgeController.class);

    private final BadgeService badgeService;

    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/all")
    public String getAllBadges(ModelMap modelMap) {
        final String RETURN_PAGE = "admin_page";
        setBadgesToRequest(modelMap);
        return RETURN_PAGE;
    }

    // Used to add badge to update form
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{badgeId}")
    public String getAllBadgesAndSetBadgeIdToRequest(ModelMap modelMap, @PathVariable Long badgeId) {
        final String RETURN_PAGE = "admin_page";
        modelMap.addAttribute(BADGE_TO_UPDATE_ID, badgeId);
        setBadgesToRequest(modelMap);
        return RETURN_PAGE;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/add")
    public String addBadge(@Valid BadgeRequestModel badgeFromForm,
                           BindingResult br,
                           ModelMap modelMap,
                           RedirectAttributesModelMap redirectModelMap) {
        final String RETURN_PAGE_FAIL_TO_ADD = "admin_page";
        final String RETURN_PAGE_SUCCESS_ADD = "redirect:/badges/all";
        setBadgesToRequest(modelMap);

        if (br.hasFieldErrors()) {
            final String errorMessage = br.getFieldError().getDefaultMessage();
            modelMap.addAttribute(ERROR_ATTRIBUTE, errorMessage);
            return RETURN_PAGE_FAIL_TO_ADD;
        }

        final BadgeDto badgeFromDB = badgeService.createBadge(badgeFromForm.convertToBadgeDto());
        if (badgeFromDB == null) {
            modelMap.addAttribute(ERROR_ATTRIBUTE, BADGE_NAME_IS_TAKEN);
            return RETURN_PAGE_FAIL_TO_ADD;
        }
        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, BADGE_CREATED);
        log.info("Added badge id: {} to DB, at: {}", badgeFromDB.getId(), LocalDateTime.now());
        return RETURN_PAGE_SUCCESS_ADD;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{badgeId}/update")
    public String updateBadge(@PathVariable Long badgeId,
                              @Valid BadgeRequestModel badgeFromForm,
                              BindingResult br,
                              ModelMap modelMap,
                              RedirectAttributesModelMap redirectModelMap) {

        final String RETURN_PAGE_FAIL_TO_UPDATE = "admin_page";
        final String RETURN_PAGE_SUCCESS_UPDATE = "redirect:/badges/all";
        setBadgesToRequest(modelMap);

        if (br.hasFieldErrors()) {
            final String errorMessage = br.getFieldError().getDefaultMessage();
            modelMap.addAttribute(ERROR_ATTRIBUTE, errorMessage);
            return RETURN_PAGE_FAIL_TO_UPDATE;
        }
        if (badgeService.checkNameIsTaken(badgeFromForm.getBadgeName())) {
            modelMap.addAttribute(ERROR_ATTRIBUTE, BADGE_NAME_IS_TAKEN);
            return RETURN_PAGE_FAIL_TO_UPDATE;
        }

        final BadgeDto badgeToUpdate = badgeFromForm.convertToBadgeDto();
        badgeToUpdate.setId(badgeId);
        final boolean isUpdated = badgeService.updateBadge(badgeToUpdate);

        if (!isUpdated) {
            modelMap.addAttribute(ERROR_ATTRIBUTE, FAILED_TO_UPDATE);
            log.error("Failed to update badge id: {} , at: {}", badgeId, LocalDateTime.now());
            return RETURN_PAGE_FAIL_TO_UPDATE;
        }
        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, SUCCESSFUL_UPDATE);
        log.info("Updated badge id: {} , at: {}", badgeId, LocalDateTime.now());
        return RETURN_PAGE_SUCCESS_UPDATE;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{badgeId}/delete")
    public String deleteBadge(@PathVariable Long badgeId,
                              RedirectAttributesModelMap redirectModelMap) {

        final String RETURN_PAGE = "redirect:/badges/all";

        final boolean isDeleted = badgeService.deleteBadge(badgeId);
        if (!isDeleted) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, FAILED_TO_DELETE);
            log.error("Failed to delete badge id: {} , at: {}", badgeId, LocalDateTime.now());
        }
        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, SUCCESSFUL_DELETE);
        log.info("User id: {} deleted at: {}", badgeId, LocalDateTime.now());
        return RETURN_PAGE;
    }

    private void setBadgesToRequest(ModelMap modelMap) {
        final List<BadgeDto> badgesDB = badgeService.getAllBadges();
        modelMap.addAttribute(BADGES_ALL_FROM_DB, badgesDB);
    }
}
