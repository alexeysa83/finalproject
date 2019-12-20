package com.github.alexeysa83.finalproject.web.controller.entity_controller;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.model.dto.CommentRatingDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.comment.CommentService;
import com.github.alexeysa83.finalproject.web.Utils.WebAuthenticationUtils;
import com.github.alexeysa83.finalproject.web.request_entity.CommentRequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static com.github.alexeysa83.finalproject.web.Utils.MessageContainer.*;
import static com.github.alexeysa83.finalproject.web.Utils.WebAuthenticationUtils.getPrincipalUserAuthId;
import static com.github.alexeysa83.finalproject.web.Utils.WebAuthenticationUtils.isPrincipalUserAdmin;

@Controller
@RequestMapping(value = "/comments")
public class CommentController {

    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(value = "/{commentId}")
    public String setCommentIdToUpdateInNewsView(@PathVariable Long commentId,
                                                 @RequestParam(value = "newsId") Long newsId,
                                                 @RequestParam(value = "authorId") Long authorId,
                                                 ModelMap modelMap) {
        final String RETURN_PAGE_FORWARD_TO_NEWS = "forward:/news/" + newsId;
        if ((!getPrincipalUserAuthId().equals(authorId)) & !(isPrincipalUserAdmin())) {
            modelMap.addAttribute(ERROR_ATTRIBUTE, NO_PERMISSION_TO_UPDATE);
            return RETURN_PAGE_FORWARD_TO_NEWS;
        }
        modelMap.addAttribute(COMMENT_TO_UPDATE_ID, commentId);
        return RETURN_PAGE_FORWARD_TO_NEWS;
    }

    @PostMapping(value = "")
    public String addComment(@RequestParam(value = "newsId") Long newsId,
                             @Valid CommentRequestModel commentFromAddForm,
                             BindingResult br,
                             RedirectAttributesModelMap redirectModelMap) {

        final String RETURN_PAGE_REDIRECT_NEWS = "redirect:/news/" + newsId;

        if (br.hasFieldErrors()) {
            final String errorMessage = br.getFieldError().getDefaultMessage();
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, errorMessage);
            return RETURN_PAGE_REDIRECT_NEWS;
        }

        final CommentDto commentDto = commentFromAddForm.convertToCommentDto();
        commentDto.setNewsId(newsId);
        final Timestamp creationTime = UtilService.getTime();
        commentDto.setCreationTime(creationTime);
        final AuthUserDto userInSession = WebAuthenticationUtils.getPrincipalUserInSession();
        commentDto.setAuthId(userInSession.getId());
        commentDto.setAuthorComment(userInSession.getLogin());

        final CommentDto commentFromDB = commentService.createComment(commentDto);
        if (commentFromDB == null) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, ERROR_UNKNOWN);
            log.error("Failed to create comment to news id: {} , at: {}", newsId, LocalDateTime.now());
        }
        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, COMMENT_CREATED);
        log.info("Created comment to news id: {} , at: {}", newsId, LocalDateTime.now());
        return RETURN_PAGE_REDIRECT_NEWS;
    }

    /**
     * Too many params? Better take from DB
     */
        @PostMapping(value = "/{commentId}/update")
    public String updateComment(@PathVariable Long commentId,
                                @RequestParam(value = "newsId") Long newsId,
                                @RequestParam(value = "authorId") Long authorId,
                                @Valid CommentRequestModel commentFromAddForm,
                                BindingResult br,
                                RedirectAttributesModelMap redirectModelMap) {

        final String RETURN_PAGE_REDIRECT_NEWS = "redirect:/news/" + newsId;

        if ((!getPrincipalUserAuthId().equals(authorId)) & !(isPrincipalUserAdmin())) {
            redirectModelMap.addAttribute(ERROR_ATTRIBUTE, NO_PERMISSION_TO_UPDATE);
            return RETURN_PAGE_REDIRECT_NEWS;
        }
        if (br.hasFieldErrors()) {
            final String errorMessage = br.getFieldError().getDefaultMessage();
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, errorMessage);
            return RETURN_PAGE_REDIRECT_NEWS;
        }

        final CommentDto commentToUpdate = commentFromAddForm.convertToCommentDto();
        commentToUpdate.setId(commentId);
        final boolean isUpdated = commentService.updateComment(commentToUpdate);
        if (!isUpdated) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, FAILED_TO_UPDATE);
            log.error("Failed to update comment id: {} , at: {}", commentId, LocalDateTime.now());
        }
        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, SUCCESSFUL_UPDATE);
        log.info("Updated comment id: {} , at: {}", commentId, LocalDateTime.now());
        return RETURN_PAGE_REDIRECT_NEWS;
    }

     @PostMapping(value = "/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId,
                                @RequestParam(value = "newsId") Long newsId,
                                @RequestParam(value = "authorId") Long authorId,
                                RedirectAttributesModelMap redirectModelMap) {

        final String RETURN_PAGE_REDIRECT_NEWS = "redirect:/news/" + newsId;

        if ((!getPrincipalUserAuthId().equals(authorId)) & !(isPrincipalUserAdmin())) {
            redirectModelMap.addAttribute(ERROR_ATTRIBUTE, NO_PERMISSION_TO_UPDATE);
            return RETURN_PAGE_REDIRECT_NEWS;
        }

        final boolean isDeleted = commentService.deleteComment(commentId);
        if (!isDeleted) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, FAILED_TO_DELETE);
            log.error("Failed to delete comment id: {} , at: {}", commentId, LocalDateTime.now());
        }
        redirectModelMap.addFlashAttribute(MESSAGE_ATTRIBUTE, SUCCESSFUL_UPDATE);
        log.info("Deleted comment id: {} , at: {}", commentId, LocalDateTime.now());
        return RETURN_PAGE_REDIRECT_NEWS;
    }

    @GetMapping ("/{commentId}/add_rating/{authId}")
    public String addRatingToComment (@RequestParam(value = "newsId") Long newsId,
                                      @RequestParam(value = "authorId") Long authorId,
                                      CommentRatingDto commentRatingDto,
                                      RedirectAttributesModelMap redirectModelMap){
        final String RETURN_PAGE = "redirect:/news/" + newsId;

        if ((getPrincipalUserAuthId().equals(authorId))) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, NO_PERMISSION_TO_UPDATE);
            return RETURN_PAGE;
        }

        final boolean isAdded = commentService.addRatingOnComment(commentRatingDto);
        if (!isAdded) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, ERROR_UNKNOWN);
            log.error("Failed to add rating to comment: {}, at: {}", commentRatingDto, LocalDateTime.now());
        }
        return RETURN_PAGE;
    }

    @GetMapping ("/{commentId}/delete_rating/{authId}")
    public String deleteRatingFromComment (@RequestParam(value = "newsId") Long newsId,
                                           @RequestParam(value = "authorId") Long authorId,
                                           CommentRatingDto commentRatingDto,
                                           RedirectAttributesModelMap redirectModelMap){
        final String RETURN_PAGE = "redirect:/news/" + newsId;

        if ((getPrincipalUserAuthId().equals(authorId))) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, NO_PERMISSION_TO_UPDATE);
            return RETURN_PAGE;
        }

        final boolean isDeleted = commentService.deleteRatingFromComment(commentRatingDto);
        if (!isDeleted) {
            redirectModelMap.addFlashAttribute(ERROR_ATTRIBUTE, ERROR_UNKNOWN);
            log.error("Failed to add rating to comment: {}, at: {}", commentRatingDto, LocalDateTime.now());
        }
        return RETURN_PAGE;
    }
}
