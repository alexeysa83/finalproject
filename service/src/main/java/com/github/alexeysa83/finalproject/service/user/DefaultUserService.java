package com.github.alexeysa83.finalproject.service.user;

import com.github.alexeysa83.finalproject.dao.user.DefaultUserDao;
import com.github.alexeysa83.finalproject.dao.user.UserDao;
import com.github.alexeysa83.finalproject.model.User;
import com.github.alexeysa83.finalproject.service.UtilsService;

public class DefaultUserService implements UserService {

    private UserDao userDAO = DefaultUserDao.getInstance();

    private static volatile UserService instance;

    public static UserService getInstance() {
        UserService localInstance = instance;
        if (localInstance == null) {
            synchronized (UserService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultUserService();
                }
            }
        }
        return localInstance;
    }

    //   User create/delete service is made in transaction with AuthUserDAO methods

    @Override
    public User getById(String value) {
        final long id = UtilsService.stringToLong(value);
        return userDAO.getById(id);
    }

    @Override
    public boolean update(User user) {
        return userDAO.update(user);
    }

}
