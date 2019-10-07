package com.github.alexeysa83.finalproject.service.impl;


import com.github.alexeysa83.finalproject.dao.AuthUserDao;
import com.github.alexeysa83.finalproject.dao.impl.DefaultAuthUserDao;
import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.service.SecurityService;

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
    public AuthUser createAndSaveAuthUser(AuthUser user) {
        return authUserDao.createAndSave(user);
    }

    @Override
    public AuthUser login(AuthUser userFromLogin) {
        AuthUser userFromDB = authUserDao.getByLogin(userFromLogin);
        if (userFromDB == null || userFromDB.isBlocked()) {
            return null;
        }
        if (userFromDB.getPassword().equals(userFromLogin.getPassword())) {
            return userFromDB;
        }
        return null;
    }

    @Override
    public boolean checkLoginIsTaken(String login) {
        return authUserDao.checkLoginIsTaken(login);
    }

    }
