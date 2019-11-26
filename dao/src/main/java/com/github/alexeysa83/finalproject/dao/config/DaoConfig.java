package com.github.alexeysa83.finalproject.dao.config;

import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.badge.BadgeBaseDao;
import com.github.alexeysa83.finalproject.dao.badge.DefaultBadgeBaseDao;
import com.github.alexeysa83.finalproject.dao.comment.CommentBaseDao;
import com.github.alexeysa83.finalproject.dao.comment.DefaultCommentBaseDao;
import com.github.alexeysa83.finalproject.dao.news.DefaultNewsBaseDao;
import com.github.alexeysa83.finalproject.dao.news.NewsBaseDao;
import com.github.alexeysa83.finalproject.dao.user.DefaultUserInfoBaseDao;
import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
public class DaoConfig {

    @Bean
    public AuthUserBaseDao authUserBaseDao (EntityManagerFactory factory) {
        return new DefaultAuthUserBaseDao(factory);
    }

    @Bean
    public UserInfoBaseDao userInfoBaseDao () {
        return new DefaultUserInfoBaseDao();
    }

    @Bean
    public NewsBaseDao newsBaseDao () {
        return new DefaultNewsBaseDao();
    }

    @Bean
    public CommentBaseDao commentBaseDao () {
        return new DefaultCommentBaseDao();
    }

    @Bean
    public BadgeBaseDao badgeBaseDao () {
        return new DefaultBadgeBaseDao();
    }
}
