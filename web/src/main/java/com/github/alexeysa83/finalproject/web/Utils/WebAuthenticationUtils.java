package com.github.alexeysa83.finalproject.web.Utils;

import com.github.alexeysa83.finalproject.model.Role;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

public abstract class WebAuthenticationUtils {

    private WebAuthenticationUtils() {
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

    public static void setUserInSession(AuthUserDto principal) {
        final Authentication authentication = new UsernamePasswordAuthenticationToken
                (principal, null, getAuthorities(principal.getRole()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private static List<GrantedAuthority> getAuthorities(Role role) {
        switch (role) {
            case USER:
                return Collections.singletonList((GrantedAuthority) () -> "ROLE_USER");
            case ADMIN:
                return Collections.singletonList((GrantedAuthority) () -> "ROLE_ADMIN");
            default:
                throw new RuntimeException("Wrong role");
        }
    }
}
