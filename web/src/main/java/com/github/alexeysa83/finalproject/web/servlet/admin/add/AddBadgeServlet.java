package com.github.alexeysa83.finalproject.web.servlet.admin.add;

import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.badge.DefaultBadgeService;
import com.github.alexeysa83.finalproject.service.validation.BadgeValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.*;

@WebServlet(name = "AddBadgeServlet", urlPatterns = {"/admin/add/badge"})
public class AddBadgeServlet extends HttpServlet {

    private BadgeService badgeService = DefaultBadgeService.getInstance();
    private BadgeValidationService validationService = new BadgeValidationService();
    private static final Logger log = LoggerFactory.getLogger(AddBadgeServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String badgeName = req.getParameter("badgeName");
        String message = validationService.isNameValid(badgeName);
        if (message!=null) {
            forwardToServletMessage("/admin/main", message, req, resp);
            return;
        }

        final BadgeDto badge = new BadgeDto();
        badge.setBadgeName(badgeName);
        final BadgeDto savedBadge = badgeService.createBadge(badge);
        if (savedBadge == null) {
            message = "error.unknown";
//            forwardToJspMessage("adminpage", message, req, resp);
            forwardToServletMessage("/admin/main", message, req, resp);
            log.error("Failed to add badge name: {}, at: {}", badgeName, LocalDateTime.now());
            return;
        }
        redirect("/admin/main", req, resp);
    }
}
