package com.github.alexeysa83.finalproject.dao;

import com.github.alexeysa83.finalproject.model.AuthUser;

public interface AuthUserDao extends DAO<AuthUser> {

    AuthUser getByLogin(AuthUser user);

    boolean checkLoginIsTaken(String login);
}
