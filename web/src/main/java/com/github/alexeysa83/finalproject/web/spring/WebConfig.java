package com.github.alexeysa83.finalproject.web.spring;

import com.github.alexeysa83.finalproject.service.config.ServiceConfig;
import com.github.alexeysa83.finalproject.web.controller.entity_controller.*;
import com.github.alexeysa83.finalproject.web.controller.security.ErrorController;
import com.github.alexeysa83.finalproject.web.controller.security.LoginController;
import com.github.alexeysa83.finalproject.web.controller.security.LogoutController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

import java.util.Locale;

@Configuration
@EnableWebMvc
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebConfig {

    private final ServiceConfig serviceConfig;

    public WebConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    // Security controllers beans
    @Bean
    public LoginController loginController() {
        return new LoginController(serviceConfig.authUserService());
    }

    @Bean
    public LogoutController logoutController() {
        return new LogoutController();
    }

    @Bean
    public ErrorController errorController() {
        return new ErrorController();
    }

    // Entity controllers beans
    @Bean
    public AuthUserController authUserController() {
        return new AuthUserController(serviceConfig.authUserService());
    }

    @Bean
    public UserInfoController userInfoController() {
        return new UserInfoController(serviceConfig.userService(), serviceConfig.badgeService());
    }

    @Bean
    public NewsController newsController() {
        return new NewsController(serviceConfig.newsService(), serviceConfig.commentService());
    }

    @Bean
    public CommentController commentController() {
        return new CommentController(serviceConfig.commentService());
    }

    @Bean
    public BadgeController badgeController() {
        return new BadgeController(serviceConfig.badgeService());
    }

    // Web config beans
    @Bean
    public UrlBasedViewResolver tilesViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setViewClass(TilesView.class);
        return resolver;
    }

    @Bean
    public TilesConfigurer tilesConfigurer() {
        final TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions("/WEB-INF/tiles.xml");
        return tilesConfigurer;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasenames("classpath:/messages", "classpath:/interface");
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    @Bean
    public CookieLocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.forLanguageTag("en_US"));
        resolver.setCookieName("LocaleCookie");
        resolver.setCookieMaxAge(3600);
        return resolver;
    }
}
