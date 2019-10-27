package com.github.alexeysa83.finalproject.web.servlet.auth.comment;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.comment.DefaultCommentService;
import com.github.alexeysa83.finalproject.service.comment.CommentService;
import com.github.alexeysa83.finalproject.service.validation.CommentValidationService;
import com.github.alexeysa83.finalproject.web.servlet.auth.news.AddNewsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.*;

@WebServlet(name = "AddCommentServlet", urlPatterns = {"/auth/comment/add"})
public class AddCommentServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AddNewsServlet.class);
    private CommentService commentService = DefaultCommentService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String content = req.getParameter("content");
        String message = CommentValidationService.isValidContent(content);
        if (message != null) {
            forwardToServletMessage("/news/view", message, req, resp);
            return;
        }

        AuthUserDto authUser = (AuthUserDto) req.getSession().getAttribute("authUser");
        final Timestamp creationTime = UtilService.getTime();
        final String newsId = req.getParameter("newsId");
        final long id = UtilService.stringToLong(newsId);
        final CommentDto comment = commentService.createAndSave
                (new CommentDto(content, creationTime, authUser.getId(), id, authUser.getLogin()));

        String logMessage = "Created comment to news id: {} , at: {}";
        if (comment == null) {
            logMessage = "Failed to create comment to news id: {} , at: {}";
        }
        log.info(logMessage, newsId, LocalDateTime.now());
        forwardToServlet("/news/view", req, resp);
    }
}
