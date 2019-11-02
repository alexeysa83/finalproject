package com.github.alexeysa83.finalproject.web.servlet.auth.news;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.news.DefaultNewsService;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import com.github.alexeysa83.finalproject.service.validation.NewsValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.*;

@WebServlet(name = "AddNewsServlet", urlPatterns = {"/auth/news/add"})
public class AddNewsServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AddNewsServlet.class);
    private NewsService newsService = DefaultNewsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        forwardToJsp("addnews", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String title = req.getParameter("title");
        final String content = req.getParameter("content");
        String message = NewsValidationService.isValidTitleContent(title, content);
        if (message != null) {
            forwardToJspMessage("addnews", message, req, resp);
            return;
        }

        AuthUserDto user = (AuthUserDto) req.getSession().getAttribute("authUser");
        final Timestamp creationTime = UtilService.getTime();
        final NewsDto news = newsService.createNews(new NewsDto(title, content, creationTime, user.getId(), user.getLogin()));
        if (news == null) {
            req.setAttribute("message", "error.unknown");
            log.error("Failed to add news for user id: {}, at: {}", user.getId(), LocalDateTime.now());
            doGet(req, resp);
            return;
        }
        log.info("Added news id: {}, at: {}", news.getId(), LocalDateTime.now());
        redirect("/index.jsp", req, resp);
    }
}
