package com.github.alexeysa83.finalproject.web.servlet.auth.message;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.MessageDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.message.DefaultMessageService;
import com.github.alexeysa83.finalproject.service.message.MessageService;
import com.github.alexeysa83.finalproject.service.validation.MessageValidationService;
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

@WebServlet(name = "AddMessageServlet", urlPatterns = {"/auth/message/add"})
public class AddMessageServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AddNewsServlet.class);
    private MessageService messageService = DefaultMessageService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final String content = req.getParameter("content");
        String message = MessageValidationService.isValidContent(content);
        if (message != null) {
            forwardToServletMessage("/news/view", message, req, resp);
            return;
        }

        AuthUserDto authUser = (AuthUserDto) req.getSession().getAttribute("authUser");
        final Timestamp creationTime = UtilService.getTime();
        final String newsId = req.getParameter("newsId");
        final long id = UtilService.stringToLong(newsId);
        final MessageDto mess = messageService.createAndSave
                (new MessageDto(content, creationTime, authUser.getId(), id, authUser.getLogin()));

        String logMessage = "Created message for news id: {} , at: {}";
        if (mess == null) {
            logMessage = "Failed to create message for news id: {} , at: {}";
        }
        log.info(logMessage, newsId, LocalDateTime.now());
        forwardToServlet("/news/view", req, resp);
    }
}
