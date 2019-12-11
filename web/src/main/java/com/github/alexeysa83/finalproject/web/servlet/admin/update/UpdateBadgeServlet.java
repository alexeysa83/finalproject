package com.github.alexeysa83.finalproject.web.servlet.admin.update;

import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.validation.BadgeValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Controller
@RequestMapping
public class UpdateBadgeServlet {

    @Autowired
    private BadgeService badgeService;

    private BadgeValidationService validationService = new BadgeValidationService();

    private static final Logger log = LoggerFactory.getLogger(UpdateBadgeServlet.class);

    @GetMapping("/admin/update/badge")
    public String doGet(HttpServletRequest req) {
        final String badgeId = req.getParameter("badgeId");
        req.setAttribute("badgeToUpdateId", badgeId);
        return "forward:/admin/main";
    }

    @PostMapping("/admin/update/badge")
    public String  doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String badgeName = req.getParameter("badgeName");
        String message = validationService.isNameValid(badgeName);
        if (message!=null) {
            req.setAttribute("message", message);
            return "forward:/admin/main";
        }
        final String badgeId = req.getParameter("badgeId");
        final long id = UtilService.stringToLong(badgeId);

        final BadgeDto badgeToUpdate = new BadgeDto();
        badgeToUpdate.setId(id);
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
        return "forward:/admin/main";
    }
}
