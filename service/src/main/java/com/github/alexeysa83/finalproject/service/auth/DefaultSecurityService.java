package com.github.alexeysa83.finalproject.service.auth;


import com.github.alexeysa83.finalproject.dao.authuser.AuthUserDao;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserDao;
import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.service.UtilsService;
import com.github.alexeysa83.finalproject.service.ValidationService;

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

//    @Override
//    public AuthUser getByLogin(String login) {
//        return authUserDao.getByLogin(login);
//    }

    @Override
    public AuthUser getById(String value) {
        final long authId = UtilsService.stringToLong(value);
        return authUserDao.getById(authId);
    }

    @Override
    public AuthUser login(AuthUser userFromLogin) {
        AuthUser userFromDB = authUserDao.getByLogin(userFromLogin.getLogin());
        boolean isValidPassword = false;
        if (userFromDB != null) {
            isValidPassword = ValidationService.isPasswordEqual
                    (userFromLogin.getPassword(), userFromDB.getPassword());
        }
        return (isValidPassword) ? userFromDB : null;
    }

    @Override
    public boolean checkLoginIsTaken(String login) {
        return authUserDao.getByLogin(login) != null;
    }

    @Override
    public boolean update(AuthUser user) {
        return authUserDao.update(user);
    }

    @Override
    public boolean delete(String value) {
        final long authId = UtilsService.stringToLong(value);
        return authUserDao.delete(authId);
    }
}
