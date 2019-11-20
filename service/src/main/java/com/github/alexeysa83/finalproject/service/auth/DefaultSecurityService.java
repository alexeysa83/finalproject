package com.github.alexeysa83.finalproject.service.auth;


import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import com.github.alexeysa83.finalproject.service.UtilService;
import com.github.alexeysa83.finalproject.service.validation.AuthValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class DefaultSecurityService implements SecurityService {

    @Autowired
    private AuthUserBaseDao authUserDao;

    @Override
    public AuthUserDto createAuthUser(String login, String password) {
        final Timestamp regTime = UtilService.getTime();
        final UserInfoDto userInfoDto = new UserInfoDto(regTime);
        AuthUserDto authUserDto = new AuthUserDto(login, password, userInfoDto);
        return authUserDao.add(authUserDto);
    }

    @Override
    public AuthUserDto getById(long id) {
        return authUserDao.getById(id);
    }

    @Override
    public AuthUserDto loginAuthUser(AuthUserDto userFromLogin) {
        AuthUserDto userFromDB = authUserDao.getByLogin(userFromLogin.getLogin());
        boolean isValidPassword = false;
        if (userFromDB != null) {
            isValidPassword = new AuthValidationService().isPasswordEqual
                    (userFromLogin.getPassword(), userFromDB.getPassword());
        }
        return (isValidPassword) ? userFromDB : null;
    }

    @Override
    public boolean checkLoginIsTaken(String login) {
        return authUserDao.getByLogin(login) != null;
    }

    @Override
    public boolean updateAuthUser(AuthUserDto user) {
        return authUserDao.update(user);
    }

    @Override
    public boolean deleteUser(long id) {
        return authUserDao.delete(id);
    }
}
