package com.github.alexeysa83.finalproject.dao.Utils;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class DaoAuthenticationUtils {

    private DaoAuthenticationUtils() {
    }

    public static AuthUserDto getPrincipalUserInSession() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        return (AuthUserDto) authentication.getPrincipal();
    }

    public static Long getPrincipalUserAuthId() {
        final AuthUserDto principalUserInSession = getPrincipalUserInSession();
        if (principalUserInSession == null) {
            return 0L;
        }
        return principalUserInSession.getId();
    }

    public static boolean isPrincipalUserAdmin() {
        final AuthUserDto principalUserInSession = getPrincipalUserInSession();
        if (principalUserInSession == null) {
            return false;
        }
        final String role = principalUserInSession.getRole().toString();
        return role.equals("ADMIN");
    }
}
