package com.github.alexeysa83.finalproject.web.servlet.admin.delete;

import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Controller
@RequestMapping
public class DeleteBadgeServlet {

    @Autowired
    private BadgeService badgeService;

    private static final Logger log = LoggerFactory.getLogger(DeleteBadgeServlet.class);

    @GetMapping("/admin/delete/badge")
    public String doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String id = req.getParameter("badgeId");
        final long badgeId = UtilService.stringToLong(id);
        final boolean isDeleted = badgeService.deleteBadge(badgeId);
        String message = "delete.success";
        String logMessage = "Deleted badge id: {} , at: {}";
        if (!isDeleted) {
            message = "delete.fail";
            logMessage = "Failed to delete badge id: {} , at: {}";
        }
        log.info(logMessage, badgeId, LocalDateTime.now());
        req.setAttribute("message", message);
        return "forward:/admin/main";
    }
}
