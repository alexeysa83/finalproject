package com.github.alexeysa83.finalproject.web.controller;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.comment.CommentService;
import com.github.alexeysa83.finalproject.service.validation.CommentValidationService;
import com.github.alexeysa83.finalproject.web.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
@RequestMapping(value = "/comments")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //    "/auth/comment/update" GET
    @GetMapping(value = "/{commentId}")
    public String setCommentIdToRequest(HttpServletRequest req,
                                        @PathVariable Long commentId,
                                        @RequestParam(value = "newsId") Long newsId) {
        req.setAttribute("commentToUpdateId", commentId);
        return "forward:/news/" + newsId;
    }

    //    "/auth/comment/add" POST
    @PostMapping(value = "")
    public String addComment(HttpServletRequest req, @RequestParam(value = "newsId") Long newsId) {
        final String content = req.getParameter("content");
        String message = CommentValidationService.isValidContent(content);
        if (message != null) {
            req.setAttribute("message", message);
            return "forward:/news/" + newsId;
        }

        final AuthUserDto userInSession = WebUtils.getUserInSession();
        final Timestamp creationTime = UtilService.getTime();
        final CommentDto comment = commentService.createComment
                (new CommentDto(content, creationTime, userInSession.getId(), newsId, userInSession.getLogin()));

        String logMessage = "Created comment to news id: {} , at: {}";
        if (comment == null) {
            logMessage = "Failed to create comment to news id: {} , at: {}";
        }
        log.info(logMessage, newsId, LocalDateTime.now());
        req.setAttribute("message", message);
        return "forward:/news/" + newsId;
    }

    //    "/auth/comment/update" POST
    @PostMapping(value = "/{commentId}/update")
    public String updateComment(HttpServletRequest req,
                                @PathVariable Long commentId,
                                @RequestParam(value = "newsId") Long newsId) {
        final String content = req.getParameter("content");
        String message = CommentValidationService.isValidContent(content);
        if (message != null) {
            req.setAttribute("message", message);
            return "forward:/news/" + newsId;
        }

        final CommentDto commentToUpdate = new CommentDto();
        commentToUpdate.setId(commentId);
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
        return "forward:/news/" + newsId;
    }

    //    "/auth/comment/delete" GET
    @PostMapping(value = "/{commentId}/delete")
    public String deleteComment(HttpServletRequest req,
                                @PathVariable Long commentId,
                                @RequestParam(value = "newsId") Long newsId) {
        final boolean isDeleted = commentService.deleteComment(commentId);
        String message = "delete.success";
        String logMessage = "Deleted comment id: {} , at: {}";
        if (!isDeleted) {
            message = "delete.fail";
            logMessage = "Failed to delete comment id: {} , at: {}";
        }
        log.info(logMessage, commentId, LocalDateTime.now());
        req.setAttribute("message", message);
        return "forward:/news/" + newsId;
    }
}
