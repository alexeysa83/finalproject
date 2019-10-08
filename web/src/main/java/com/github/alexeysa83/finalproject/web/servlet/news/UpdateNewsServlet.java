package com.github.alexeysa83.finalproject.web.servlet.news;

import com.github.alexeysa83.finalproject.model.News;
import com.github.alexeysa83.finalproject.service.UtilsService;
import com.github.alexeysa83.finalproject.service.news.DefaultNewsService;
import com.github.alexeysa83.finalproject.service.news.NewsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebServlet(name = "UpdateNewsServlet", urlPatterns = {"/updatenews"})
public class UpdateNewsServlet extends HttpServlet {

    private NewsService service = DefaultNewsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String newsId = req.getParameter("newsId");
        final News news = service.getNewsOnId(newsId);
        req.setAttribute("news", news);
        forwardToJsp("updatenews", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String title = req.getParameter("title");
        final String content = req.getParameter("content");
        final String newsId = req.getParameter("newsId");

        if (title != null && content != null) {
            final long id = UtilsService.stringToLong(newsId);
            final String message = service.updateNews(new News(id, title, content));
            req.setAttribute("message", message);
            // news == null + validation service
        } else {
            req.setAttribute("message", "Title or content is not completed");
            forwardToJsp("index", req, resp);
        }
        try {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
