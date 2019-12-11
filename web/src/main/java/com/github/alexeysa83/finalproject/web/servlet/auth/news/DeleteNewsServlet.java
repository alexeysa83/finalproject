package com.github.alexeysa83.finalproject.web.servlet.auth.news;

import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping
public class DeleteNewsServlet {

    @Autowired
    private NewsService newsService;

    private static final Logger log = LoggerFactory.getLogger(DeleteNewsServlet.class);

    @GetMapping("/auth/news/delete")
    public String doGet(HttpServletRequest req) {
        final String newsId = req.getParameter("newsId");
        final long id = UtilService.stringToLong(newsId);
        final boolean isDeleted = newsService.deleteNews(id);
        String message = "delete.success";
        String logMessage = "Deleted news id: {} , at: {}";
        if (!isDeleted) {
            message = "delete.fail";
            logMessage = "Failed to delete news id: {} , at: {}";
        }
        log.info(logMessage, newsId, LocalDateTime.now());
        req.setAttribute("message", message);
        return "index";
           }
}
