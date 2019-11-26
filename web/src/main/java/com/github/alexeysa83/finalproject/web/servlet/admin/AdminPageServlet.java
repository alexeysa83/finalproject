package com.github.alexeysa83.finalproject.web.servlet.admin;

import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping
public class AdminPageServlet {
    @Autowired
    private BadgeService badgeService;

    @GetMapping("/admin/main")
    public String  doGet(HttpServletRequest req) {
        final List<BadgeDto> badgesDB = badgeService.getAllBadges();
        req.setAttribute("badgesDB", badgesDB);
        return "adminpage";
         }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
//        doGet(req, resp);
//    }
}
