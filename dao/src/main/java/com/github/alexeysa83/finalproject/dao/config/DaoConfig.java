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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(HibernateConfig.class)
public class DaoConfig {

    @Autowired
    private AuthUserRepository authUserRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BadgeRepository badgeRepository;

    @Bean
    public AuthUserBaseDao authUserBaseDao() {
        return new DefaultAuthUserBaseDao(authUserRepository);
    }

    @Bean
    public UserInfoBaseDao userInfoBaseDao() {
        return new DefaultUserInfoBaseDao(userInfoRepository);
    }

    @Bean
    public NewsBaseDao newsBaseDao() {
        return new DefaultNewsBaseDao(newsRepository);
    }

    @Bean
    public CommentBaseDao commentBaseDao() {
        return new DefaultCommentBaseDao(commentRepository);
    }

    @Bean
    public BadgeBaseDao badgeBaseDao() {
        return new DefaultBadgeBaseDao(badgeRepository);
    }
}
