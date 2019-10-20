package com.github.alexeysa83.finalproject.web.servlet;

import com.github.alexeysa83.finalproject.model.Message;
import com.github.alexeysa83.finalproject.model.News;
import com.github.alexeysa83.finalproject.service.UtilsService;
import com.github.alexeysa83.finalproject.service.message.DefaultMessageService;
import com.github.alexeysa83.finalproject.service.message.MessageService;
import com.github.alexeysa83.finalproject.service.news.DefaultNewsService;
import com.github.alexeysa83.finalproject.service.news.NewsService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebServlet (name = "NewsViewServlet", urlPatterns = {"/news/view"})
public class NewsViewServlet extends HttpServlet {

    private NewsService newsService = DefaultNewsService.getInstance();
    private MessageService messageService = DefaultMessageService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String newsId = req.getParameter("newsId");
        final long id = UtilsService.stringToLong(newsId);
        final News news = this.newsService.getNewsOnId(id);
        req.setAttribute("news", news);
        final List<Message> messageList = messageService.getMessagesOnNews(id);
        req.setAttribute("messageList", messageList);
        forwardToJsp("newsview", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }
}
