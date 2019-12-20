package com.github.alexeysa83.finalproject.web.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login",
                        "/auth_users/forward_to_registration_form",
                        "/auth_users/registration",
//                        "/news",
//                        "/news/all",
//                        "/news/forward_to_add_news_form",
//                        "/news/**",
                        "/news/*").permitAll()
                .antMatchers("/badges/**",
                        "/user_infos/*/add/*",
                        "/user_infos/*/delete/*",
                        "/auth_users/*/update_role").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/errors/access_denied")
                .and()
                .csrf().disable();
    }
}
