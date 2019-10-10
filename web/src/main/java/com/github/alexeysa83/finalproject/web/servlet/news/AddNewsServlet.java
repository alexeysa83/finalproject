package com.github.alexeysa83.finalproject.web.servlet.news;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.News;
import com.github.alexeysa83.finalproject.service.ValidationService;
import com.github.alexeysa83.finalproject.service.news.DefaultNewsService;
import com.github.alexeysa83.finalproject.service.news.NewsService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJspMessage;

@WebServlet(name = "AddNewsServlet", urlPatterns = {"/restricted/news/add"})
public class AddNewsServlet extends HttpServlet {

    private NewsService service = DefaultNewsService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String title = req.getParameter("title");
        final String content = req.getParameter("content");
        String message = ValidationService.isValidTitleContent(title, content);
        if (message != null) {
            forwardToJspMessage("addnews", message, req, resp);
            return;
        }

        AuthUser user = (AuthUser) req.getSession().getAttribute("authUser");
        final Timestamp creationTime = new Timestamp(System.currentTimeMillis());
        final News news = service.createAndSave(new News(title, content, creationTime, user.getId()));
        // news == null  + message

        try {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
