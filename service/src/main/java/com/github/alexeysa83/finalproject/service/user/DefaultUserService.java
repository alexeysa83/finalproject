package com.github.alexeysa83.finalproject.service.user;

import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserService implements UserService {

    @Autowired
    private UserInfoBaseDao userDAO;

    //   User create/delete service is made in transaction with AuthUserDAO methods

    @Override
    public UserInfoDto getById(long id) {
        return userDAO.getById(id);
    }

    @Override
    public boolean updateUserInfo(UserInfoDto user) {
        return userDAO.update(user);
    }

    @Override
    public UserInfoDto addBadgeToUser(long authId, long badgeId) {
        return userDAO.addBadgeToUser(authId, badgeId);
    }

    @Override
    public UserInfoDto deleteBadgeFromUser(long authId, long badgeId) {
        return userDAO.deleteBadgeFromUser(authId, badgeId);
    }
}
