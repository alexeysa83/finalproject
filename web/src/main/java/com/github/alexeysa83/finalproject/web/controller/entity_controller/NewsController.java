package com.github.alexeysa83.finalproject.web.controller.entity_controller;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.model.dto.NewsRatingDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import com.github.alexeysa83.finalproject.web.Utils.WebAuthenticationUtils;
import com.github.alexeysa83.finalproject.web.request_entity.NewsRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.github.alexeysa83.finalproject.web.Utils.WebAuthenticationUtils.getPrincipalUserAuthId;
import static com.github.alexeysa83.finalproject.web.Utils.WebAuthenticationUtils.isPrincipalUserAdmin;
import static com.github.alexeysa83.finalproject.web.Utils.MessageContainer.*;

@Controller
@RequestMapping(value = "/news")
public class NewsController {

    private static final Logger log = LoggerFactory.getLogger(NewsController.class);

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
            }

    @GetMapping(value = "/all")
    public String getNewsOnMainPage(@RequestParam(name = "currentPage", defaultValue = "1") String currentPage,
                                    ModelMap modelMap) {
        final String RETURN_PAGE = "main_page";

        final int page = Integer.parseInt(currentPage);
        modelMap.addAttribute(PAGE_CURRENT, page);

        List<NewsDto> newsList = newsService.getNewsOnCurrentPage(page);
        modelMap.addAttribute(NEWS_LIST, newsList);

        int totalPages = newsService.getNewsTotalPages();
        modelMap.addAttribute(PAGES_TOTAL, totalPages);

        return RETURN_PAGE;
    }

    @GetMapping(value = "/{newsId}")
    public String getNewsById(@PathVariable Long newsId,
                              ModelMap modelMap) {
        final String RETURN_PAGE = "news_view";
        final NewsDto news = newsService.getNewsOnId(newsId);
        modelMap.addAttribute(NEWS_SINGLE, news);
        return RETURN_PAGE;
    }

    @GetMapping(value = "/{newsId}/to_news_update_form")
    public String setNewsToUpdateForm(@PathVariable Long newsId,
                                      @RequestParam(value = "authorId") Long authorId,
                                      ModelMap modelMap) {
        final String RETURN_PAGE_TO_UPDATE_FORM = "news_update";
        final String RETURN_PAGE_NO_PERMISSION_TO_UPDATE = "forward:/news/" + newsId;
        if ((!getPrincipalUserAuthId().equals(authorId)) & !(isPrincipalUserAdmin())) {
            modelMap.addAttribute(ERROR_ATTRIBUTE, NO_PERMISSION_TO_UPDATE);
            return RETURN_PAGE_NO_PERMISSION_TO_UPDATE;
        }
        final NewsDto newsFromDB = newsService.getNewsOnId(newsId);
        modelMap.addAttribute(NEWS_SINGLE, newsFromDB);
        return RETURN_PAGE_TO_UPDATE_FORM;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/forward_to_add_news_form")
    public String forwardToAddNewsJSP() {
        return "add_news";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/add")
    public String addNews(@Valid NewsRequestModel newsFromAddForm,
                          BindingResult br,
                          ModelMap modelMap,
                          RedirectAttributesModelMap redirectModelMap) {

        final String RETURN_PAGE_FAIL_TO_ADD = "add_news";
        final String RETURN_PAGE_SUCCESS_ADD = "redirect:/news/";
        if (br.hasFieldErrors()) {
            final String errorMessage = br.getFieldError().getDefaultMessage();
            modelMap.addAttribute(ERROR_ATTRIBUTE, errorMessage);
            return RETURN_PAGE_FAIL_TO_ADD;
        }

        final NewsDto newsDto = newsFromAddForm.convertToNewsDto();
        final Timestamp creationTime = UtilService.getTime();
        newsDto.setCreationTime(creationTime);
        final AuthUserDto userInSession = WebAuthenticationUtils.getPrincipalUserInSession();
        newsDto.setAuthId(userInSession.getId());
        newsDto.setAuthorNews(userInSession.getLogin());

        final NewsDto newsFromDB = newsService.createNews(newsDto);
        if (newsFromDB == null) {
            modelMap.addAttribute(ERROR_ATTRIBUTE, ERROR_UNKNOWN);
            log.error("Failed to add news for user id: {}, at: {}", userInSession.getId(), LocalDateTime.now());
            return RETURN_PAGE_FAIL_TO_ADD;
        }
        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, NEWS_CREATED);
        log.info("Added news id: {}, at: {}", newsFromDB.getId(), LocalDateTime.now());
        return RETURN_PAGE_SUCCESS_ADD + newsFromDB.getId();
    }

    @PostMapping(value = "/{newsId}/update")
    public String updateNews(@PathVariable Long newsId,
                             @RequestParam(value = "authorId") Long authorId,
                             @Valid NewsRequestModel newsFromAddForm,
                             BindingResult br,
                             RedirectAttributesModelMap redirectModelMap) {

        final String RETURN_PAGE_FAIL_TO_UPDATE = "redirect:/news/" + newsId + "/to_news_update_form";
        final String RETURN_PAGE_SUCCESS_UPDATE = "redirect:/news/" + newsId;

        if ((!getPrincipalUserAuthId().equals(authorId)) & !(isPrincipalUserAdmin())) {
            redirectModelMap.addAttribute("authorId", authorId);
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, NO_PERMISSION_TO_UPDATE);
            return RETURN_PAGE_FAIL_TO_UPDATE;
        }
        if (br.hasFieldErrors()) {
            final String errorMessage = br.getFieldError().getDefaultMessage();
            redirectModelMap.addAttribute("authorId", authorId);
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, errorMessage);
            return RETURN_PAGE_FAIL_TO_UPDATE;
        }

        final NewsDto newsToUpdate = newsFromAddForm.convertToNewsDto();
        newsToUpdate.setId(newsId);
        final boolean isUpdated = newsService.updateNews(newsToUpdate);
        if (!isUpdated) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, FAILED_TO_UPDATE);
            log.error("Failed to update news id: {} , at: {}", newsId, LocalDateTime.now());
            return RETURN_PAGE_FAIL_TO_UPDATE;
        }
        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, SUCCESSFUL_UPDATE);
        log.info("Updated news id: {} , at: {}", newsId, LocalDateTime.now());
        return RETURN_PAGE_SUCCESS_UPDATE;
    }

    @PostMapping(value = "/{newsId}/delete")
    public String deleteNews(@PathVariable Long newsId,
                             @RequestParam(value = "authorId") Long authorId,
                             RedirectAttributesModelMap redirectModelMap) {

        final String RETURN_PAGE = "redirect:/news/all";

        if ((!getPrincipalUserAuthId().equals(authorId)) & !(isPrincipalUserAdmin())) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, NO_PERMISSION_TO_UPDATE);
            return RETURN_PAGE;
        }

        final boolean isDeleted = newsService.deleteNews(newsId);
        if (!isDeleted) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, FAILED_TO_DELETE);
            log.error("Failed to delete news id: {} , at: {}", newsId, LocalDateTime.now());
            return RETURN_PAGE;
        }
        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, SUCCESSFUL_DELETE);
        log.info("Deleted news id: {} , at: {}", newsId, LocalDateTime.now());
        return RETURN_PAGE;
    }

    @GetMapping ("/{newsId}/add_rating/{authId}")
    public String addRatingToNews (NewsRatingDto newsRatingDto){
        final String RETURN_PAGE = "redirect:/news/" + newsRatingDto.getNewsId();

        final boolean isAdded = newsService.addRatingOnNews(newsRatingDto);
        return RETURN_PAGE;
    }

    @GetMapping ("/{newsId}/delete_rating/{authId}")
    public String deleteRatingFromNews (NewsRatingDto newsRatingDto){
        final String RETURN_PAGE = "redirect:/news/" + newsRatingDto.getNewsId();

        final boolean isDeleted = newsService.deleteRatingFromNews(newsRatingDto);
        return RETURN_PAGE;
    }
}
