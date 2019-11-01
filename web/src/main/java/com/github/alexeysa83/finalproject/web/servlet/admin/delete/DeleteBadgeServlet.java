package com.github.alexeysa83.finalproject.web.servlet.admin.delete;

import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.badge.DefaultBadgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServletMessage;

@WebServlet (name = "DeleteBadgeServlet", urlPatterns = {"/admin/delete/badge"})
public class DeleteBadgeServlet extends HttpServlet {

    private BadgeService badgeService = DefaultBadgeService.getInstance();

    private static final Logger log = LoggerFactory.getLogger(DeleteBadgeServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
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
        forwardToServletMessage("/admin/main", message, req, resp);
    }
}
