package com.github.alexeysa83.finalproject.web.servlet.admin.add;

import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.validation.BadgeValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping
public class AddBadgeServlet {

    @Autowired
    private BadgeService badgeService;

    private BadgeValidationService validationService = new BadgeValidationService();

    private static final Logger log = LoggerFactory.getLogger(AddBadgeServlet.class);

    @PostMapping("/admin/add/badge")
    public String doPost(HttpServletRequest req) {
        final String badgeName = req.getParameter("badgeName");
        String message = validationService.isNameValid(badgeName);
        if (message != null) {
            req.setAttribute("message", message);
            return "forward:/admin/main";
        }

        final BadgeDto badge = new BadgeDto();
        badge.setBadgeName(badgeName);
        final BadgeDto savedBadge = badgeService.createBadge(badge);
        if (savedBadge == null) {
            message = "error.unknown";
            log.error("Failed to add badge name: {}, at: {}", badgeName, LocalDateTime.now());
            req.setAttribute("message", message);
            return "forward:/admin/main";
        }
        return "redirect:/admin/main";
    }
}
