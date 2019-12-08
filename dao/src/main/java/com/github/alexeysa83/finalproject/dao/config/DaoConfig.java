package com.github.alexeysa83.finalproject.dao.config;

import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.badge.BadgeBaseDao;
import com.github.alexeysa83.finalproject.dao.badge.DefaultBadgeBaseDao;
import com.github.alexeysa83.finalproject.dao.comment.CommentBaseDao;
import com.github.alexeysa83.finalproject.dao.comment.DefaultCommentBaseDao;
import com.github.alexeysa83.finalproject.dao.news.DefaultNewsBaseDao;
import com.github.alexeysa83.finalproject.dao.news.NewsBaseDao;
import com.github.alexeysa83.finalproject.dao.repository.*;
import com.github.alexeysa83.finalproject.dao.user.DefaultUserInfoBaseDao;
import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(HibernateConfig.class)
public class DaoConfig {

    @Bean
    public AuthUserBaseDao authUserBaseDao(AuthUserRepository repository) {
        return new DefaultAuthUserBaseDao(repository);
    }

    @Bean
    public UserInfoBaseDao userInfoBaseDao(UserInfoRepository repository) {
        return new DefaultUserInfoBaseDao(repository);
    }

    @Bean
    public NewsBaseDao newsBaseDao(NewsRepository repository) {
        return new DefaultNewsBaseDao(repository);
    }

    @Bean
    public CommentBaseDao commentBaseDao(CommentRepository repository) {
        return new DefaultCommentBaseDao(repository);
    }

    @Bean
    public BadgeBaseDao badgeBaseDao(BadgeRepository repository) {
        return new DefaultBadgeBaseDao(repository);
    }
}
