package com.github.alexeysa83.finalproject.web.controller;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.comment.CommentService;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import com.github.alexeysa83.finalproject.service.validation.NewsValidationService;
import com.github.alexeysa83.finalproject.web.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping(value = "/news")
public class NewsController {

    private static final Logger log = LoggerFactory.getLogger(NewsController.class);

    private final NewsService newsService;

    private final CommentService commentService;

    public NewsController(NewsService newsService, CommentService commentService) {
        this.newsService = newsService;
        this.commentService = commentService;
    }


    /**
     * Check null
     * currentPage default value
     */
//    "/main" GET
    @GetMapping(value = "")
    public String getAllNews(HttpServletRequest req) {

        String currentPage = req.getParameter("currentPage");
        if (currentPage == null) {
            currentPage = "1";
        }
        // Parse Integer not needed?
        final int page = Integer.parseInt(currentPage);
        req.setAttribute("currentPage", page);

        List<NewsDto> newsList = newsService.getNewsOnCurrentPage(page);
        req.setAttribute("newsList", newsList);

        int totalPages = newsService.getNewsTotalPages();
        req.setAttribute("totalPages", totalPages);
        return "mainpage";
    }

    /**
     * Optimization
     */
//"/news/view" GET
    @RequestMapping (value = "/{newsId}", method = {RequestMethod.GET, RequestMethod.POST})
    public String getNewsById(HttpServletRequest req, @PathVariable Long newsId) {
        final NewsDto news = newsService.getNewsOnId(newsId);
        req.setAttribute("news", news);
        final List<CommentDto> commentList = commentService.getCommentsOnNews(newsId);
        req.setAttribute("commentList", commentList);
        return "newsview";
    }

    //    "/auth/news/update" GET
    @RequestMapping(value = "/{newsId}/torequest", method = {RequestMethod.GET, RequestMethod.POST} )
    public String setNewsToUpdate(HttpServletRequest req, @PathVariable Long newsId) {
        final NewsDto news = newsService.getNewsOnId(newsId);
        req.setAttribute("news", news);
        return "newsupdate";
    }

//        "/auth/news/update" GET
    @GetMapping(value = "/add_news_to_jsp")
    public String forwardToJSP() {
        return "addnews";
    }

    //    "/auth/news/add" POST
    @PostMapping(value = "")
    public String addNews(HttpServletRequest req) {
        final String title = req.getParameter("title");
        final String content = req.getParameter("content");
        String message = NewsValidationService.isValidTitleContent(title, content);
        if (message != null) {
            req.setAttribute("message", message);
            return "addnews";
        }

        final AuthUserDto userInSession = WebUtils.getUserInSession();
        final Timestamp creationTime = UtilService.getTime();
        final NewsDto news = newsService.createNews(
                new NewsDto(title, content, creationTime, userInSession.getId(), userInSession.getLogin()));
        if (news == null) {
            req.setAttribute("message", "error.unknown");
            log.error("Failed to add news for user id: {}, at: {}", userInSession.getId(), LocalDateTime.now());
            return "addnews";
        }
        log.info("Added news id: {}, at: {}", news.getId(), LocalDateTime.now());
        return "redirect:/index.jsp";
    }

    /**
     * Optimization
     *
     * @param req
     * @return
     */
//    "/auth/news/update" POST
    @PostMapping(value = "/{newsId}/update")
    public String updateNews(HttpServletRequest req, @PathVariable Long newsId) {
        final String title = req.getParameter("title");
        final String content = req.getParameter("content");
        String message = NewsValidationService.isValidTitleContent(title, content);
        if (message != null) {
            req.setAttribute("message", message);
            return "forward:/news/" + newsId + "/torequest";
        }

        final boolean isUpdated = newsService.updateNews(new NewsDto(newsId, title, content));

        message = "update.success";
        String logMessage = "Updated news id: {} , at: {}";
        if (!isUpdated) {
            message = "update.fail";
            logMessage = "Failed to update news id: {} , at: {}";
        }
        log.info(logMessage, newsId, LocalDateTime.now());
        req.setAttribute("message", message);
        return "forward:/news/" + newsId;
    }

    //    "/auth/news/delete" GET
    @PostMapping(value = "/{newsId}/delete")
    public String deleteNews(HttpServletRequest req, @PathVariable Long newsId) {
        final boolean isDeleted = newsService.deleteNews(newsId);
        String message = "delete.success";
        String logMessage = "Deleted news id: {} , at: {}";
        if (!isDeleted) {
            message = "delete.fail";
            logMessage = "Failed to delete news id: {} , at: {}";
        }
        log.info(logMessage, newsId, LocalDateTime.now());
        req.setAttribute("message", message);
        return "forward:/news";
    }
}
