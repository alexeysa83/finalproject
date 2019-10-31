package com.github.alexeysa83.finalproject.web.servlet;

import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import com.github.alexeysa83.finalproject.service.news.DefaultNewsService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebServlet(name = "MainPageServlet", urlPatterns = "/main")
public class MainPageServlet extends HttpServlet {

    private NewsService newsService = DefaultNewsService.getInstance();

    // Check null
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        String currentPage = req.getParameter("currentPage");
        if (currentPage == null) {
            currentPage = "1";
        }
        final int page = Integer.parseInt(currentPage);
        req.setAttribute("currentPage", page);

        List<NewsDto> newsList = newsService.getNewsOnCurrentPage(page);
        req.setAttribute("newsList", newsList);

        int totalPages = newsService.getNewsTotalPages();
        req.setAttribute("totalPages", totalPages);
        forwardToJsp("mainpage", req, resp);
    }
}
