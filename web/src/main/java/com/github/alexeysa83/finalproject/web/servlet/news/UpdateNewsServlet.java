package com.github.alexeysa83.finalproject.web.servlet.news;

import com.github.alexeysa83.finalproject.model.News;
import com.github.alexeysa83.finalproject.service.UtilsService;
import com.github.alexeysa83.finalproject.service.ValidationService;
import com.github.alexeysa83.finalproject.service.news.DefaultNewsService;
import com.github.alexeysa83.finalproject.service.news.NewsService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;
import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServletMessage;

@WebServlet(name = "UpdateNewsServlet", urlPatterns = {"/restricted/news/update"})
public class UpdateNewsServlet extends HttpServlet {

    private NewsService service = DefaultNewsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String newsId = req.getParameter("newsId");
        final News news = service.getNewsOnId(newsId);
        req.setAttribute("news", news);
        forwardToJsp("newsupdate", req, resp);
    }

    // Optimization
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String title = req.getParameter("title");
        final String content = req.getParameter("content");
        String message = ValidationService.isValidTitleContent(title, content);
        if (message != null) {
            req.setAttribute("message", message);
            doGet(req, resp);
            return;
        }

        final String newsId = req.getParameter("newsId");
        final long id = UtilsService.stringToLong(newsId);
        final boolean isUpdated = service.updateNews(new News(id, title, content));

        message = "Update succesfull";
        if (!isUpdated) {
            message = "Update cancelled, please try again";
        }
        forwardToServletMessage("/news/view", message, req, resp);
    }
}
