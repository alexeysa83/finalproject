package com.github.alexeysa83.finalproject.web.servlet.auth.news;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import com.github.alexeysa83.finalproject.service.validation.NewsValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
@RequestMapping
public class AddNewsServlet {

    @Autowired
    private NewsService newsService;

    private static final Logger log = LoggerFactory.getLogger(AddNewsServlet.class);

    @GetMapping("/auth/news/add")
    public String doGet() {
        return "addnews";
    }

    @PostMapping("/auth/news/add")
    public String doPost(HttpServletRequest req) {
        final String title = req.getParameter("title");
        final String content = req.getParameter("content");
        String message = NewsValidationService.isValidTitleContent(title, content);
        if (message != null) {
            req.setAttribute("message", message);
            return "addnews";
        }

        AuthUserDto user = (AuthUserDto) req.getSession().getAttribute("authUser");
        final Timestamp creationTime = UtilService.getTime();
        final NewsDto news = newsService.createNews(new NewsDto(title, content, creationTime, user.getId(), user.getLogin()));
        if (news == null) {
            req.setAttribute("message", "error.unknown");
            log.error("Failed to add news for user id: {}, at: {}", user.getId(), LocalDateTime.now());
            return "addnews";
        }
        log.info("Added news id: {}, at: {}", news.getId(), LocalDateTime.now());
        return "redirect:/index.jsp";
    }
}
