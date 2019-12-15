package com.github.alexeysa83.finalproject.web;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class WebUtils {

                private WebUtils() {
    }

    public static Long getCurrentUserId () {
       return getUserInSession().getId();
    }

    public static String getCurrentUserLogin () {
        return getUserInSession().getLogin();
    }

    public static AuthUserDto getUserInSession() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (AuthUserDto)authentication.getPrincipal();
    }
   }
