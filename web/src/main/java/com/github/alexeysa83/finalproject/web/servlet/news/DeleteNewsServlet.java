package com.github.alexeysa83.finalproject.web.servlet.news;

import com.github.alexeysa83.finalproject.service.news.DefaultNewsService;
import com.github.alexeysa83.finalproject.service.news.NewsService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebServlet (name = "DeleteNewsServlet", urlPatterns = {"/restricted/news/delete"})
public class DeleteNewsServlet extends HttpServlet {

    private NewsService service = DefaultNewsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String id = req.getParameter("newsId");
        final boolean isDeleted = service.deleteNews(id);
        String message = "Delete successfull";
        if (!isDeleted) {
            message = "Delete cancelled";
        }
        req.setAttribute("message", message);
        forwardToJsp("index", req, resp);
    }
}
