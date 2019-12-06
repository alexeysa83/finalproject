package com.github.alexeysa83.finalproject.dao;

import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.badge.BadgeBaseDao;
import com.github.alexeysa83.finalproject.dao.comment.CommentBaseDao;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.news.NewsBaseDao;
import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import com.github.alexeysa83.finalproject.model.dto.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
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
    @Autowired
    private SessionFactory factory;

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

    public void completeDeleteUser(long id) {
        final EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        AuthUserEntity toDelete = entityManager.find(AuthUserEntity.class, id);
        entityManager.remove(toDelete);
        entityManager.getTransaction().commit();
        entityManager.clear();
    }

    // Test UserInfo util methods
    public UserInfoDto createUserInfoDto() {
        return new UserInfoDto(getTime());
    }

    public UserInfoDto addTestUserInfoToDB(String name) {
        final AuthUserDto testAuthUser = addTestAuthUserToDB(name);
        final UserInfoDto userInfoDto = createUserInfoDto();
        userInfoDto.setAuthId(testAuthUser.getId());
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
}
