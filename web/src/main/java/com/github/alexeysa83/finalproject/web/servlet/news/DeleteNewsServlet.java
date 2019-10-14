package com.github.alexeysa83.finalproject.web.servlet.news;

import com.github.alexeysa83.finalproject.service.news.DefaultNewsService;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import com.github.alexeysa83.finalproject.web.servlet.auth.RegistrationServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebServlet (name = "DeleteNewsServlet", urlPatterns = {"/restricted/news/delete"})
public class DeleteNewsServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DeleteNewsServlet.class);
    private NewsService service = DefaultNewsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String newsId = req.getParameter("newsId");
        final boolean isDeleted = service.deleteNews(newsId);
        String message = "delete.success";
        String logMessage = "Deleted news id: {} , at: {}";
        if (!isDeleted) {
            message = "delete.fail";
            logMessage = "Failed to delete news id: {} , at: {}";
        }
        log.info(logMessage, newsId, LocalDateTime.now());
        req.setAttribute("message", message);
        forwardToJsp("index", req, resp);
    }
}
