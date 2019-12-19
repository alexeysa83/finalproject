package com.github.alexeysa83.finalproject.service.auth;


import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

public class DefaultAuthUserService implements AuthUserService {

    private final AuthUserBaseDao authUserDao;

    private final UserInfoBaseDao userInfoDao;

    public DefaultAuthUserService(AuthUserBaseDao authUserDao, UserInfoBaseDao userInfoDao) {
        this.authUserDao = authUserDao;
        this.userInfoDao = userInfoDao;
    }

    @Override
    @Transactional
    public AuthUserDto createAuthUserAndUserInfo(AuthUserDto userFromRegistrationForm) {
        if (checkLoginIsTaken(userFromRegistrationForm.getLogin())) {
            return null;
        }

        final AuthUserDto savedAuthUser = authUserDao.add(userFromRegistrationForm);

        final Timestamp regTime = UtilService.getTime();
        final UserInfoDto userInfoDto = new UserInfoDto(regTime);
        userInfoDto.setAuthId(savedAuthUser.getId());
        final UserInfoDto savedUserInfo = userInfoDao.add(userInfoDto);

        savedAuthUser.setUserInfoDto(savedUserInfo);
        return savedAuthUser;
    }

    @Override
    @Transactional
    public AuthUserDto getById(Long id) {
        return authUserDao.getById(id);
    }

    @Override
    @Transactional
    public AuthUserDto loginAuthUser(AuthUserDto userFromLogin) {
        AuthUserDto userFromDB = authUserDao.getByLogin(userFromLogin.getLogin());
        boolean isValidPassword = false;
        if (userFromDB != null) {
            isValidPassword =
                    userFromLogin.getPassword().equals(userFromDB.getPassword());
        }
        return (isValidPassword) ? userFromDB : null;
    }

    @Override
    @Transactional
    public boolean checkLoginIsTaken(String login) {
        return authUserDao.getByLogin(login) != null;
    }

    @Override
    @Transactional
    public boolean updateAuthUser(AuthUserDto user) {
        return authUserDao.update(user);
    }

    /**
     *
     */
    @Override
    @Transactional
    public boolean deleteUser(Long id) {
        final boolean isDeletedAuthUser = authUserDao.delete(id);
        if (!isDeletedAuthUser) {
            return false;
        }
        return userInfoDao.delete(id);
    }
}
