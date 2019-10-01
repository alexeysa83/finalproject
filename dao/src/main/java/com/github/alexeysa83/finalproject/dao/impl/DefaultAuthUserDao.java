package com.github.alexeysa83.finalproject.dao.impl;

import com.github.alexeysa83.finalproject.dao.AuthUserDao;
import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.Role;

import java.util.HashMap;
import java.util.Map;

public class DefaultAuthUserDao implements AuthUserDao {

    private Map<String, AuthUser> userByLogin;

    private DefaultAuthUserDao() {
        this.userByLogin = new HashMap<>();
        userByLogin.put("admin", new AuthUser("admin", "admin", Role.ADMIN));
        userByLogin.put("user", new AuthUser("user", "user", Role.USER));
    }

    private static volatile AuthUserDao instance;

    public static AuthUserDao getInstance() {
        AuthUserDao localInstance = instance;
        if (localInstance == null) {
            synchronized (AuthUserDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultAuthUserDao();
                }
            }
        }
        return localInstance;
    }

    @Override
    public void saveAuthUser(AuthUser user) {
        userByLogin.put(user.getLogin(), user);
    }

    @Override
    public AuthUser createAuthUser(String login, String password, boolean isAdmin) {
        saveAuthUser(new AuthUser(login, password,(isAdmin) ? Role.ADMIN : Role.USER));
        return getAuthUserByLogin(login);
    }

    @Override
    public AuthUser getAuthUserByLogin(String login) {
        return userByLogin.get(login);
    }

    @Override
    public boolean checkLoginIsTaken(String login) {
        return userByLogin.containsKey(login);
    }
}
