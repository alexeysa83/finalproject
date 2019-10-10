package com.github.alexeysa83.finalproject.web.servlet;

import com.github.alexeysa83.finalproject.model.News;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import com.github.alexeysa83.finalproject.service.news.DefaultNewsService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebServlet(name = "MainPageServlet", urlPatterns = "/news/main")
public class MainPageServlet extends HttpServlet {

    private NewsService service = DefaultNewsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<News> newsList = service.getNewsOnPage();
        req.setAttribute("newsList", newsList);
        forwardToJsp("mainpage", req, resp);
    }
}
