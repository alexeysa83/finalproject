package com.github.alexeysa83.finalproject.web.servlet.admin.update;

import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.service.UtilService;
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

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServlet;
import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServletMessage;

@WebServlet (name = "UpdateBadgeServlet", urlPatterns = {"/admin/update/badge"})
public class UpdateBadgeServlet extends HttpServlet {

    private BadgeService badgeService = DefaultBadgeService.getInstance();
    private BadgeValidationService validationService = new BadgeValidationService();
    private static final Logger log = LoggerFactory.getLogger(UpdateBadgeServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String badgeId = req.getParameter("badgeId");
        req.setAttribute("badgeToUpdate", badgeId);
        forwardToServlet("/admin/main", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String badgeName = req.getParameter("badgeName");
        String message = validationService.isNameValid(badgeName);
        if (message!=null) {
            forwardToServletMessage("/admin/main", message, req, resp);
            return;
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
        forwardToServletMessage("/admin/main", message, req, resp);
    }
}
