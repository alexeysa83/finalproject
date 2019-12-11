package com.github.alexeysa83.finalproject.web.servlet;

import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping ("/main")
public class MainPageServlet {

    @Autowired
    private NewsService newsService;

    // Check null
    @GetMapping
    public String doGet(HttpServletRequest req) {

        String currentPage = req.getParameter("currentPage");
        if (currentPage == null) {
            currentPage = "1";
        }
        // Parse Integer not needed?
        final int page = Integer.parseInt(currentPage);
        req.setAttribute("currentPage", page);

        List<NewsDto> newsList = newsService.getNewsOnCurrentPage(page);
        req.setAttribute("newsList", newsList);

        int totalPages = newsService.getNewsTotalPages();
        req.setAttribute("totalPages", totalPages);
        return "mainpage";
    }
}
