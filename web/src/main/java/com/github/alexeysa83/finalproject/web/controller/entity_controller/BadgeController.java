package com.github.alexeysa83.finalproject.web.controller.entity_controller;


import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.validation.BadgeValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping (value = "/badges")
public class BadgeController {

    private static final Logger log = LoggerFactory.getLogger(BadgeController.class);

    private final BadgeValidationService validationService;

    private final BadgeService badgeService;

    public BadgeController(BadgeValidationService validationService, BadgeService badgeService) {
        this.validationService = validationService;
        this.badgeService = badgeService;
    }

    //    "/admin/main" GET
    @GetMapping(value = "")
    public String getAllBadges(HttpServletRequest req) {
//        final List<BadgeDto> badgesDB = badgeService.getAllBadges();
//        req.setAttribute("badgesDB", badgesDB);
        setBadgesToRequest(req);
        return "admin_page";
    }

    //"/admin/update/badge" GET
    @GetMapping(value = "/{badgeId}")
    public String setBadgeIdToRequest(HttpServletRequest req, @PathVariable Long badgeId) {
        req.setAttribute("badgeToUpdateId", badgeId);
        setBadgesToRequest(req);
//        return "adminpage";
        return "forward:/badges";
    }

//    "/admin/add/badge" POST
    @PostMapping(value = "")
    public String addBadge(HttpServletRequest req) {
        final String badgeName = req.getParameter("badgeName");
        String message = validationService.isNameValid(badgeName);
        if (message != null) {
            req.setAttribute("message", message);
            setBadgesToRequest(req);
            return "admin_page";
//            return "forward:/admin/main";
        }

        final BadgeDto badge = new BadgeDto();
        badge.setBadgeName(badgeName);
        final BadgeDto savedBadge = badgeService.createBadge(badge);
        if (savedBadge == null) {
            message = "error.unknown";
            log.error("Failed to add badge name: {}, at: {}", badgeName, LocalDateTime.now());
            req.setAttribute("message", message);
            setBadgesToRequest(req);
            return "admin_page";
//            return "forward:/badges";
        }
        return "redirect:/badges";
    }

//    "/admin/update/badge" POST
    @PostMapping(value = "/{badgeId}/update")
    public String updateBadge(HttpServletRequest req, @PathVariable Long badgeId) {
        final String badgeName = req.getParameter("badgeName");
        String message = validationService.isNameValid(badgeName);
        if (message!=null) {
            req.setAttribute("message", message);
            setBadgesToRequest(req);
            return "admin_page";
//            return "forward:/admin/main";
        }

        final BadgeDto badgeToUpdate = new BadgeDto();
        badgeToUpdate.setId(badgeId);
        badgeToUpdate.setBadgeName(badgeName);

        final boolean isUpdated = badgeService.updateBadge(badgeToUpdate);
        message = "update.success";
        String logMessage = "Updated badge id: {} , at: {}";
        if (!isUpdated) {
            message = "update.fail";
            logMessage = "Failed to update badge id: {} , at: {}";
        }
        log.info(logMessage, badgeId, LocalDateTime.now());
        req.setAttribute("message", message);
        setBadgesToRequest(req);
        return "admin_page";
//        return "forward:/admin/main";
    }

//    "/admin/delete/badge" GET
    @PostMapping(value = "/{badgeId}/delete")
    public String deleteBadge(HttpServletRequest req, @PathVariable Long badgeId) {
        final boolean isDeleted = badgeService.deleteBadge(badgeId);
        String message = "delete.success";
        String logMessage = "Deleted badge id: {} , at: {}";
        if (!isDeleted) {
            message = "delete.fail";
            logMessage = "Failed to delete badge id: {} , at: {}";
        }
        log.info(logMessage, badgeId, LocalDateTime.now());
        req.setAttribute("message", message);
        setBadgesToRequest(req);
        return "admin_page";
//        return "forward:/badges";
    }

    private void setBadgesToRequest(HttpServletRequest req) {
        final List<BadgeDto> badgesDB = badgeService.getAllBadges();
        req.setAttribute("badgesDB", badgesDB);
    }
}
