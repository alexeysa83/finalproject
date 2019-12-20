package com.github.alexeysa83.finalproject.dao.util;

import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.badge.BadgeBaseDao;
import com.github.alexeysa83.finalproject.dao.comment.CommentBaseDao;
import com.github.alexeysa83.finalproject.dao.news.NewsBaseDao;
import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import com.github.alexeysa83.finalproject.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Component
public class AddDeleteTestEntity {

    @Autowired
    private AuthUserBaseDao authUserDao;
    @Autowired
    private UserInfoBaseDao userDAO;
    @Autowired
    private NewsBaseDao newsDao;
    @Autowired
    private CommentBaseDao commentDao;
    @Autowired
    private BadgeBaseDao badgeDao;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Timestamp getTime() {
        String time = sdf.format(System.currentTimeMillis());
        return Timestamp.valueOf(time);
    }

    // Test AuthUser util methods
    public AuthUserDto createAuthUserDto(String name) {
        return new AuthUserDto(name, name + "Pass");
    }

    public AuthUserDto addTestAuthUserToDB(String name) {
        final AuthUserDto user = createAuthUserDto(name);
        return authUserDao.add(user);
    }

    // Test UserInfo util methods
    public UserInfoDto createUserInfoDto() {
        return new UserInfoDto(getTime());
    }

    public UserInfoDto addTestUserInfoToDB(Long authId) {
        final UserInfoDto userInfoDto = createUserInfoDto();
        userInfoDto.setAuthId(authId);
        return userDAO.add(userInfoDto);
    }

    // Test News util methods
    public NewsDto createNewsDto(String title, AuthUserDto author) {
        final NewsDto newsDto = new NewsDto();
        newsDto.setTitle(title);
        newsDto.setContent(title + "Content");
        newsDto.setCreationTime(getTime());
        newsDto.setAuthId(author.getId());
        newsDto.setAuthorNews(author.getLogin());
        return newsDto;
    }

    public NewsDto addTestNewsToDB(String title, AuthUserDto author) {
        final NewsDto newsDto = createNewsDto(title, author);
        return newsDao.add(newsDto);
    }

    // Test Comment util methods
    // For the test purposes news author and comment author is always the same
    public CommentDto createCommentDto(String content, NewsDto news) {
        final CommentDto commentDto = new CommentDto();
        commentDto.setContent(content);
        commentDto.setCreationTime(getTime());
        commentDto.setAuthId(news.getAuthId());
        commentDto.setNewsId(news.getId());
        commentDto.setAuthorComment(news.getAuthorNews());
        return commentDto;
    }

    public CommentDto addTestCommentToDB(String content, NewsDto news) {
        final CommentDto commentDto = createCommentDto(content, news);
        return commentDao.add(commentDto);
    }

    // Test Badge util methods
    public BadgeDto createBadgeDto(String name) {
        BadgeDto badgeDto = new BadgeDto();
        badgeDto.setBadgeName(name);
        return badgeDto;
    }

    public BadgeDto addTestBadgeToDB(String name) {
        final BadgeDto badgeDto = createBadgeDto(name);
        return badgeDao.add(badgeDto);
    }

    // Test News Rating util methods
    public NewsRatingDto createNewsRatingDto(Long authId, Long newsId, Integer rate) {
        NewsRatingDto newsRatingDto = new NewsRatingDto();
        newsRatingDto.setAuthId(authId);
        newsRatingDto.setNewsId(newsId);
        newsRatingDto.setRate(rate);
        return newsRatingDto;
    }

    public boolean addTestNewsRatingToDB(Long authId, Long newsId, Integer rate) {
        final NewsRatingDto newsRatingDto = createNewsRatingDto(authId, newsId, rate);
        return newsDao.addRatingOnNews(newsRatingDto);
    }

    // Test Comment Rating util methods
    public CommentRatingDto createCommentRatingDto(Long authId, Long commentId, Integer rate) {
        CommentRatingDto commentRatingDto = new CommentRatingDto();
        commentRatingDto.setAuthId(authId);
        commentRatingDto.setCommentId(commentId);
        commentRatingDto.setRate(rate);
        return commentRatingDto;
    }

    public boolean addTestCommentRatingToDB(Long authId, Long commentId, Integer rate) {
        final CommentRatingDto commentRatingDto = createCommentRatingDto(authId, commentId, rate);
        return commentDao.addRatingOnComment(commentRatingDto);
    }
}
