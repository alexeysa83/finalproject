package com.github.alexeysa83.finalproject.web.servlet.news;

import com.github.alexeysa83.finalproject.model.News;
import com.github.alexeysa83.finalproject.service.UtilsService;
import com.github.alexeysa83.finalproject.service.news.DefaultNewsService;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import com.github.alexeysa83.finalproject.service.validation.NewsValidationservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;
import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServletMessage;

@WebServlet(name = "UpdateNewsServlet", urlPatterns = {"/restricted/news/update"})
public class UpdateNewsServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UpdateNewsServlet.class);
    private NewsService service = DefaultNewsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String newsId = req.getParameter("newsId");
        final long id = UtilsService.stringToLong(newsId);
        final News news = service.getNewsOnId(id);
        req.setAttribute("news", news);
        forwardToJsp("newsupdate", req, resp);
    }

    // Optimization
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String title = req.getParameter("title");
        final String content = req.getParameter("content");
        String message = NewsValidationservice.isValidTitleContent(title, content);
        if (message != null) {
            req.setAttribute("message", message);
            doGet(req, resp);
            return;
        }

        final String newsId = req.getParameter("newsId");
        final long id = UtilsService.stringToLong(newsId);
        final boolean isUpdated = service.updateNews(new News(id, title, content));

        message = "update.success";
        String logMessage = "Updated news id: {} , at: {}";
        if (!isUpdated) {
            message = "update.fail";
            logMessage = "Failed to update news id: {} , at: {}";
        }
        log.info(logMessage, newsId, LocalDateTime.now());
        forwardToServletMessage("/news/view", message, req, resp);
    }
}
