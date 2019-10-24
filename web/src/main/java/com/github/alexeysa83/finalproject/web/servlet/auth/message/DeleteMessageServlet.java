package com.github.alexeysa83.finalproject.web.servlet.auth.message;

import com.github.alexeysa83.finalproject.service.UtilsService;
import com.github.alexeysa83.finalproject.service.message.DefaultMessageService;
import com.github.alexeysa83.finalproject.service.message.MessageService;
import com.github.alexeysa83.finalproject.web.servlet.auth.news.DeleteNewsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.WebUtils.forwardToServletMessage;

@WebServlet(name = "DeleteMessageServlet", urlPatterns = {"/auth/message/delete"})
public class DeleteMessageServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DeleteNewsServlet.class);
    private MessageService messageService = DefaultMessageService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String messageId = req.getParameter("messageId");
        final long messId = UtilsService.stringToLong(messageId);
        final boolean isDeleted = messageService.deleteMessage(messId);
        String message = "delete.success";
        String logMessage = "Deleted message id: {} , at: {}";
        if (!isDeleted) {
            message = "delete.fail";
            logMessage = "Failed to delete message id: {} , at: {}";
        }
        log.info(logMessage, messageId, LocalDateTime.now());
        forwardToServletMessage("/news/view", message, req, resp);
    }
}
