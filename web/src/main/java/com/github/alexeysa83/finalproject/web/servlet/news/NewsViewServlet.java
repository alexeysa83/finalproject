package com.github.alexeysa83.finalproject.web.servlet.news;

import com.github.alexeysa83.finalproject.model.News;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import com.github.alexeysa83.finalproject.service.news.DefaultNewsService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebServlet (name = "NewsViewServlet", urlPatterns = {"/newsview"})
public class NewsViewServlet extends HttpServlet {

    private NewsService service = DefaultNewsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String newsId = req.getParameter("newsId");
        News news = service.getNewsOnId(newsId);
        req.setAttribute("news", news);
        forwardToJsp("newsview", req, resp);
    }
}
