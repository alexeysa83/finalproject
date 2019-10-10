package com.github.alexeysa83.finalproject.service;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.service.auth.DefaultSecurityService;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;

public abstract class ValidationService {


    private static SecurityService securityService = DefaultSecurityService.getInstance();

    private ValidationService() {
    }

    /*
    Authuser +add Regex patterns
     */
    public static String isLoginValid(String login) {
        String message = null;
        if (login.length() < 1) {
            message = "Unacceptable login";
        } else if (securityService.checkLoginIsTaken(login)) {
            message = "Login is already taken";
        }
        return message;
    }

    public static String isPasswordValid(String pass1, String pass2) {
        String message = null;
        if (pass1.length() < 1) {
            message = "Unacceptable password";
        } else if (!pass1.equals(pass2)) {
            message = "Repeat password do not match";
        }
        return message;
    }

    public static boolean isPasswordEqual(String pass1, String pass2) {
        return pass1.equals(pass2);
    }

    //Check if user in session is the same to user who's security settings are being changed
    public static boolean needLogout(AuthUser user, String id) {
        final long authId = UtilsService.stringToLong(id);
        return authId == user.getId();
    }

    public static boolean isRoleValid(String role) {
        return role.equals("USER") || role.equals("ADMIN");
    }
        /*
    User
     */

    /*
    News
     */

    // ++ Regex pattern
    public static String isValidTitleContent(String title, String content) {
        String message = null;
        if ((title.length() < 1) || (content.length() < 1)) {
            message = "Title or content is not completed";
        }
        return message;
    }

    /*
    Message
     */

}
