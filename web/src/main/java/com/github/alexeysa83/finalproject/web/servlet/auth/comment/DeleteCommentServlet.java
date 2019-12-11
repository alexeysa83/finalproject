package com.github.alexeysa83.finalproject.web.servlet.auth.comment;

import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.comment.CommentService;
import com.github.alexeysa83.finalproject.web.servlet.auth.news.DeleteNewsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping
public class DeleteCommentServlet {

    @Autowired
    private CommentService commentService;

    private static final Logger log = LoggerFactory.getLogger(DeleteNewsServlet.class);

    @GetMapping("/auth/comment/delete")
    public String doGet(HttpServletRequest req) {
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
        req.setAttribute("message", message);
        return "forward:/news/view";
    }
}
