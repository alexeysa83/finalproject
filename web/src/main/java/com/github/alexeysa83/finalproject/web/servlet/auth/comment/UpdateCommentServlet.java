package com.github.alexeysa83.finalproject.web.servlet.auth.comment;

import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.comment.CommentService;
import com.github.alexeysa83.finalproject.service.validation.CommentValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequestMapping
public class UpdateCommentServlet {

    @Autowired
    private CommentService commentService;

    private static final Logger log = LoggerFactory.getLogger(UpdateCommentServlet.class);

    @GetMapping("/auth/comment/update")
    public String doGet(HttpServletRequest req) {
        final String commentId = req.getParameter("commentId");
        req.setAttribute("commentToUpdateId", commentId);
        return "forward:/news/view";
    }

    @PostMapping("/auth/comment/update")
    public String doPost(HttpServletRequest req) {
        final String content = req.getParameter("content");
        String message = CommentValidationService.isValidContent(content);
        if (message != null) {
            req.setAttribute("message", message);
            return "forward:/news/view";
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
        req.setAttribute("message", message);
        return "forward:/news/view";
    }
}
