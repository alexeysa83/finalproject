package com.github.alexeysa83.finalproject.web.servlet.admin;

import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.badge.DefaultBadgeService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebServlet(name = "AdminPageServlet", urlPatterns = {"/admin/main"})

public class AdminPageServlet extends HttpServlet {

    private BadgeService badgeService = DefaultBadgeService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final List<BadgeDto> badgesDB = badgeService.getAllBadges();
        req.setAttribute("badgesDB", badgesDB);
        forwardToJsp("adminpage", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }
}
