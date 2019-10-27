package com.github.alexeysa83.finalproject.service.user;

import com.github.alexeysa83.finalproject.dao.user.DefaultUserBaseDao;
import com.github.alexeysa83.finalproject.dao.user.UserBaseDao;
import com.github.alexeysa83.finalproject.model.dto.UserDto;

public class DefaultUserService implements UserService {

    private UserBaseDao userDAO = DefaultUserBaseDao.getInstance();

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
    public UserDto getById(long id) {
        return userDAO.getById(id);
    }

    @Override
    public boolean update(UserDto user) {
        return userDAO.update(user);
    }

}
