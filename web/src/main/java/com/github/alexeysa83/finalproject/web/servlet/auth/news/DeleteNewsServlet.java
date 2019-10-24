package com.github.alexeysa83.finalproject.web.servlet.auth.news;

import com.github.alexeysa83.finalproject.service.UtilsService;
import com.github.alexeysa83.finalproject.service.news.DefaultNewsService;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJspMessage;

@WebServlet (name = "DeleteNewsServlet", urlPatterns = {"/auth/news/delete"})
public class DeleteNewsServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DeleteNewsServlet.class);
    private NewsService newsService = DefaultNewsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String newsId = req.getParameter("newsId");
        final long id = UtilsService.stringToLong(newsId);
        final boolean isDeleted = newsService.deleteNews(id);
        String message = "delete.success";
        String logMessage = "Deleted news id: {} , at: {}";
        if (!isDeleted) {
            message = "delete.fail";
            logMessage = "Failed to delete news id: {} , at: {}";
        }
        log.info(logMessage, newsId, LocalDateTime.now());
        forwardToJspMessage("index", message, req, resp);
    }
}
