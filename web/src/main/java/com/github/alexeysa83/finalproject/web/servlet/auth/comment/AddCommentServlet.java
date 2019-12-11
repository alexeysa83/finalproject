package com.github.alexeysa83.finalproject.web.servlet.auth.comment;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.comment.CommentService;
import com.github.alexeysa83.finalproject.service.validation.CommentValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
@RequestMapping
public class AddCommentServlet {

    @Autowired
    private CommentService commentService;

    private static final Logger log = LoggerFactory.getLogger(AddCommentServlet.class);

    @PostMapping("/auth/comment/add")
    public String doPost(HttpServletRequest req) {
        final String content = req.getParameter("content");
        String message = CommentValidationService.isValidContent(content);
        if (message != null) {
            req.setAttribute("message", message);
            return "forward:/news/view";
        }

        AuthUserDto authUser = (AuthUserDto) req.getSession().getAttribute("authUser");
        final Timestamp creationTime = UtilService.getTime();
        final String newsId = req.getParameter("newsId");
        final long id = UtilService.stringToLong(newsId);

        final CommentDto comment = commentService.createComment
                (new CommentDto(content, creationTime, authUser.getId(), id, authUser.getLogin()));

        String logMessage = "Created comment to news id: {} , at: {}";
        if (comment == null) {
            logMessage = "Failed to create comment to news id: {} , at: {}";
        }
        log.info(logMessage, newsId, LocalDateTime.now());
        req.setAttribute("message", message);
        return "forward:/news/view";
    }
}
