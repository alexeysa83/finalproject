package com.github.alexeysa83.finalproject.impl;

import com.github.alexeysa83.finalproject.AuthUser;
import com.github.alexeysa83.finalproject.AuthUserDao;
import com.github.alexeysa83.finalproject.SecurityService;


public class DefaultSecurityService implements SecurityService {

    private AuthUserDao authUserDao = DefaultAuthUserDao.getInstance();

    private static volatile SecurityService instance;

    public static SecurityService getInstance() {
        SecurityService localInstance = instance;
        if (localInstance == null) {
            synchronized (SecurityService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultSecurityService();
                }
            }
        }
        return localInstance;
    }

    @Override
    public void saveAuthUser(AuthUser authUser) {
        authUserDao.saveAuthUser(authUser);
    }

    @Override
    public AuthUser createAndSaveAuthUser(String login, String password, boolean isAdmin) {
        return authUserDao.createAuthUser(login, password, isAdmin);
    }

    @Override
    public AuthUser login(String login, String password) {
        AuthUser authUser = authUserDao.getAuthUserByLogin(login);
        if (authUser == null) {
            return null;
        }
        if (authUser.getPassword().equals(password)) {
            return authUser;
        }
        return null;
    }

    @Override
    public boolean checkLoginIsTaken(String login) {
        return authUserDao.checkLoginIsTaken(login);
    }
}
