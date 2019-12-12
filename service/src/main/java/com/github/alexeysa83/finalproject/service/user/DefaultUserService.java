package com.github.alexeysa83.finalproject.service.user;

import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.springframework.transaction.annotation.Transactional;

public class DefaultUserService implements UserService {

    private UserInfoBaseDao userDAO;

    public DefaultUserService(UserInfoBaseDao userDAO) {
        this.userDAO = userDAO;
    }

    //   User create/delete service is made in transaction with AuthUserService methods

    @Override
    @Transactional
    public UserInfoDto getById(Long id) {
        return userDAO.getById(id);
    }

    @Override
    @Transactional
    public boolean updateUserInfo(UserInfoDto user) {
        return userDAO.update(user);
    }

    @Override
    @Transactional
    public UserInfoDto addBadgeToUser(Long authId, Long badgeId) {
        return userDAO.addBadgeToUser(authId, badgeId);
    }

    @Override
    @Transactional
    public UserInfoDto deleteBadgeFromUser(Long authId, Long badgeId) {
        return userDAO.deleteBadgeFromUser(authId, badgeId);
    }
}
