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
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoConfig {

    @Bean
    public AuthUserBaseDao authUserBaseDao (SessionFactory factory) {
        return new DefaultAuthUserBaseDao(factory);
    }

    @Bean
    public UserInfoBaseDao userInfoBaseDao (SessionFactory factory) {
        return new DefaultUserInfoBaseDao(factory);
    }

    @Bean
    public NewsBaseDao newsBaseDao (SessionFactory factory) {
        return new DefaultNewsBaseDao(factory);
    }

    @Bean
    public CommentBaseDao commentBaseDao (SessionFactory factory) {
        return new DefaultCommentBaseDao(factory);
    }

    @Bean
    public BadgeBaseDao badgeBaseDao (SessionFactory factory) {
        return new DefaultBadgeBaseDao(factory);
    }
}
