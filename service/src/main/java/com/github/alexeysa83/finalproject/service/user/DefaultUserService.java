package com.github.alexeysa83.finalproject.service.user;

import com.github.alexeysa83.finalproject.dao.user.DefaultUserInfoBaseDao;
import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;

public class DefaultUserService implements UserService {

    private UserInfoBaseDao userDAO = DefaultUserInfoBaseDao.getInstance();

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
    public UserInfoDto getById(long id) {
        return userDAO.getById(id);
    }

    @Override
    public boolean update(UserInfoDto user) {
        return userDAO.update(user);
    }

}
