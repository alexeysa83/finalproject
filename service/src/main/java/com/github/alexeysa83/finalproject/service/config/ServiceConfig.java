package com.github.alexeysa83.finalproject.service.config;

import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.badge.BadgeBaseDao;
import com.github.alexeysa83.finalproject.dao.comment.CommentBaseDao;
import com.github.alexeysa83.finalproject.dao.news.NewsBaseDao;
import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import com.github.alexeysa83.finalproject.service.auth.AuthUserService;
import com.github.alexeysa83.finalproject.service.auth.DefaultAuthUserService;
import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.badge.DefaultBadgeService;
import com.github.alexeysa83.finalproject.service.comment.CommentService;
import com.github.alexeysa83.finalproject.service.comment.DefaultCommentService;
import com.github.alexeysa83.finalproject.service.news.DefaultNewsService;
import com.github.alexeysa83.finalproject.service.news.NewsService;
import com.github.alexeysa83.finalproject.service.user.DefaultUserService;
import com.github.alexeysa83.finalproject.service.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public AuthUserService authUserService(AuthUserBaseDao authUserDao, UserInfoBaseDao userInfoDao){
        return new DefaultAuthUserService(authUserDao, userInfoDao);
    }

    @Bean
    public UserService userService(UserInfoBaseDao userInfoDao){
        return new DefaultUserService(userInfoDao);
    }

    @Bean
    public NewsService newsService(NewsBaseDao newsBaseDao){
        return new DefaultNewsService(newsBaseDao);
    }

    @Bean
    public CommentService commentService(CommentBaseDao commentBaseDao){
        return new DefaultCommentService(commentBaseDao);
    }

    @Bean
    public BadgeService badgeService(BadgeBaseDao badgeBaseDao){
        return new DefaultBadgeService(badgeBaseDao);
    }
}
