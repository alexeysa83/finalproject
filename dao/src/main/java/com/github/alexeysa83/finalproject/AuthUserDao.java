package com.github.alexeysa83.finalproject;

public interface AuthUserDao {
// will wait until this comment is deleted
    void saveAuthUser (AuthUser user);

    AuthUser createAuthUser (String login, String password, boolean isAdmin);

    AuthUser getAuthUserByLogin (String login);

    boolean checkLoginIsTaken (String login);
}
