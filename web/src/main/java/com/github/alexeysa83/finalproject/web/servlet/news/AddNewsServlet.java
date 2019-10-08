package com.github.alexeysa83.finalproject.web.servlet.news;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.News;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import com.github.alexeysa83.finalproject.service.news.DefaultNewsService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToJsp;

@WebServlet(name = "AddNewsServlet", urlPatterns = {"/addnews"})
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
        if (title != null && content != null) {
            AuthUser user = (AuthUser) req.getSession().getAttribute("authUser");
            final Timestamp creationTime = new Timestamp(System.currentTimeMillis());
            final News news = service.createAndSave(new News(title, content, creationTime, user.getLogin()));
            // news == null + validation service
        } else {
            req.setAttribute("message", "Title or content is not completed");
            forwardToJsp("addnews", req, resp);
            return;
        }
        try {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
