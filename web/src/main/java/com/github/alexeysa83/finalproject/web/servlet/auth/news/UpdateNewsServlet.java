package com.github.alexeysa83.finalproject.web.servlet.auth.news;

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
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Controller
@RequestMapping
public class UpdateNewsServlet {

    @Autowired
    private NewsService service;

    private static final Logger log = LoggerFactory.getLogger(UpdateNewsServlet.class);

    @GetMapping("/auth/news/update")
    public String doGet(HttpServletRequest req) {
        final String newsId = req.getParameter("newsId");
        final long id = UtilService.stringToLong(newsId);
        final NewsDto news = service.getNewsOnId(id);
        req.setAttribute("news", news);
        return "newsupdate";
    }

    // Optimization
    @PostMapping("/auth/news/update")
    public String doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String title = req.getParameter("title");
        final String content = req.getParameter("content");
        String message = NewsValidationService.isValidTitleContent(title, content);
        if (message != null) {
            req.setAttribute("message", message);
            return "forward:/auth/news/update";
        }

        final String newsId = req.getParameter("newsId");
        final long id = UtilService.stringToLong(newsId);
        final boolean isUpdated = service.updateNews(new NewsDto(id, title, content));

        message = "update.success";
        String logMessage = "Updated news id: {} , at: {}";
        if (!isUpdated) {
            message = "update.fail";
            logMessage = "Failed to update news id: {} , at: {}";
        }
        log.info(logMessage, newsId, LocalDateTime.now());
        req.setAttribute("message", message);
        return "forward:/news/view";
    }
}
