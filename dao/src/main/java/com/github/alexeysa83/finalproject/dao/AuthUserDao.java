package com.github.alexeysa83.finalproject.dao;

import com.github.alexeysa83.finalproject.model.AuthUser;

import java.sql.SQLException;

public interface AuthUserDao {

    AuthUser createAuthUser(String login, String password, String role);

    AuthUser getAuthUserByLogin(String login);

    boolean checkLoginIsTaken(String login);
}
