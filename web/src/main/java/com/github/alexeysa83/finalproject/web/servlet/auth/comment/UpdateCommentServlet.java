package com.github.alexeysa83.finalproject.web.servlet.auth.comment;

import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.comment.CommentService;
import com.github.alexeysa83.finalproject.service.comment.DefaultCommentService;
import com.github.alexeysa83.finalproject.service.validation.CommentValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServlet;
import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServletMessage;

@WebServlet (name = "UpdateCommentServlet", urlPatterns = {"/auth/comment/update"})
public class UpdateCommentServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UpdateCommentServlet.class);
    private CommentService commentService = DefaultCommentService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String commentId = req.getParameter("commentId");
        req.setAttribute("commentToUpdateId", commentId);
        forwardToServlet("/news/view", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String content = req.getParameter("content");
        String message = CommentValidationService.isValidContent(content);
        if (message != null) {
            forwardToServletMessage("/news/view", message, req, resp);
            return;
        }

        final String commentId = req.getParameter("commentId");
        final long id = UtilService.stringToLong(commentId);

        final CommentDto commentToUpdate = new CommentDto();
        commentToUpdate.setId(id);
        commentToUpdate.setContent(content);

        final boolean isUpdated = commentService.updateComment(commentToUpdate);
        message = "update.success";
        String logMessage = "Updated comment id: {} , at: {}";
        if (!isUpdated) {
            message = "update.fail";
            logMessage = "Failed to update comment id: {} , at: {}";
        }
        log.info(logMessage, commentId, LocalDateTime.now());
        forwardToServletMessage("/news/view", message, req, resp);
    }
}
