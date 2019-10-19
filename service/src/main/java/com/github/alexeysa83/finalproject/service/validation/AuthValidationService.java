package com.github.alexeysa83.finalproject.service.validation;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.Role;
import com.github.alexeysa83.finalproject.service.UtilsService;
import com.github.alexeysa83.finalproject.service.auth.DefaultSecurityService;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;

public class AuthValidationService {

      private SecurityService securityService = DefaultSecurityService.getInstance();

    // +add Regex patterns
    public String isLoginValid(String login) {
        String message = null;
        if (login.length() < 1) {
            message = "invalid.login";
        } else if (securityService.checkLoginIsTaken(login)) {
            message = "login.istaken";
        }
        return message;
    }

    public String isPasswordValid(String pass1, String pass2) {
        String message = null;
        if (pass1.length() < 1) {
            message = "invalid.pass";
        } else if (!pass1.equals(pass2)) {
            message = "invalid.repeatpass";
        }
        return message;
    }

    public boolean isPasswordEqual(String pass1, String pass2) {
        return pass1.equals(pass2);
    }

    //Check if user in session is the same to user who's security settings are being changed
    public boolean needLogout(AuthUser user, String id) {
        final long authId = UtilsService.stringToLong(id);
        return authId == user.getId();
    }

    public boolean isRoleValid(String role) {
               return role.equals((Role.USER).toString()) || role.equals((Role.ADMIN).toString());
    }

    public  boolean isAdmin(Role role) {
        return role.equals(Role.ADMIN);
    }
}
