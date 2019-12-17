package com.github.alexeysa83.finalproject.service.config;

import com.github.alexeysa83.finalproject.dao.config.DaoConfig;
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
import com.github.alexeysa83.finalproject.service.validation.AuthValidationService;
import com.github.alexeysa83.finalproject.service.validation.BadgeValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    private final DaoConfig daoConfig;

    public ServiceConfig(DaoConfig daoConfig) {
        this.daoConfig = daoConfig;
    }

    @Bean
    public AuthUserService authUserService() {
        return new DefaultAuthUserService(
                daoConfig.authUserBaseDao(),
                daoConfig.userInfoBaseDao());
    }

    @Bean
    public UserService userService() {
        return new DefaultUserService(daoConfig.userInfoBaseDao());
    }

    @Bean
    public NewsService newsService() {
        return new DefaultNewsService(daoConfig.newsBaseDao());
    }

    @Bean
    public CommentService commentService() {
        return new DefaultCommentService(daoConfig.commentBaseDao());
    }

    @Bean
    public BadgeService badgeService() {
        return new DefaultBadgeService(daoConfig.badgeBaseDao());
    }

    /**
     *
     */
    @Bean
    public BadgeValidationService badgeValidationService() {
        return new BadgeValidationService(badgeService());
    }

    @Bean
    public AuthValidationService authValidationService() {
        return new AuthValidationService(authUserService());
    }
}
