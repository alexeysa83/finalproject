package com.github.alexeysa83.finalproject.dao;

import com.github.alexeysa83.finalproject.model.AuthUser;

public interface AuthUserDao {

    void saveAuthUser(AuthUser user);

    AuthUser createAuthUser(String login, String password, boolean isAdmin);

    AuthUser getAuthUserByLogin(String login);

    boolean checkLoginIsTaken(String login);
}
