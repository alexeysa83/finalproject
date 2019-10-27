package com.github.alexeysa83.finalproject.web.servlet.auth.comment;

import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.comment.DefaultCommentService;
import com.github.alexeysa83.finalproject.service.comment.CommentService;
import com.github.alexeysa83.finalproject.web.servlet.auth.news.DeleteNewsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServletMessage;

@WebServlet(name = "DeleteCommentServlet", urlPatterns = {"/auth/comment/delete"})
public class DeleteCommentServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DeleteNewsServlet.class);
    private CommentService commentService = DefaultCommentService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String commentId = req.getParameter("commentId");
        final long commId = UtilService.stringToLong(commentId);
        final boolean isDeleted = commentService.deleteComment(commId);
        String message = "delete.success";
        String logMessage = "Deleted comment id: {} , at: {}";
        if (!isDeleted) {
            message = "delete.fail";
            logMessage = "Failed to delete comment id: {} , at: {}";
        }
        log.info(logMessage, commentId, LocalDateTime.now());
        forwardToServletMessage("/news/view", message, req, resp);
    }
}
