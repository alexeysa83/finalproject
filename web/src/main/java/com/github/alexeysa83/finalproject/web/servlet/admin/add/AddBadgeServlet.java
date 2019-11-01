package com.github.alexeysa83.finalproject.web.servlet.admin.add;

import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.badge.DefaultBadgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJspMessage;
import static com.github.alexeysa83.finalproject.web.WebUtils.redirect;

@WebServlet (name = "AddBadgeServlet", urlPatterns = {"/admin/add/badge"})
public class AddBadgeServlet extends HttpServlet {

    private BadgeService badgeService = DefaultBadgeService.getInstance();

    private static final Logger log = LoggerFactory.getLogger(AddBadgeServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String badgeName = req.getParameter("badgeName");

        // Name validation
        final BadgeDto badge = new BadgeDto();
        badge.setBadgeName(badgeName);

        final BadgeDto savedBadge = badgeService.addNewBadge(badge);
        if (savedBadge==null) {
            String message = "error.unknown";
            forwardToJspMessage("adminpage", message, req, resp);
            log.error("Failed to add badge name: {}, at: {}", badgeName, LocalDateTime.now());
            return;
        }
        redirect("/admin/main", req, resp);
    }
}
