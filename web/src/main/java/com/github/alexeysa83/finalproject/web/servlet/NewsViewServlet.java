package com.github.alexeysa83.finalproject.web.servlet;

import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.comment.CommentService;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping
public class NewsViewServlet {
    @Autowired
    private NewsService newsService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/news/view")
    public String doGet(HttpServletRequest req) {
        final String newsId = req.getParameter("newsId");
        final long id = UtilService.stringToLong(newsId);
        final NewsDto news = newsService.getNewsOnId(id);
        req.setAttribute("news", news);
        final List<CommentDto> commentList = commentService.getCommentsOnNews(id);
        req.setAttribute("commentList", commentList);
        return "newsview";
    }

//    @PostMapping("/news/view")
//    protected void doPost(HttpServletRequest req) {
//        doGet(req);
//    }
}
