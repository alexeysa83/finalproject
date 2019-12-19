package com.github.alexeysa83.finalproject.service.auth;

import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import com.github.alexeysa83.finalproject.model.Role;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultAuthUserServiceTest {

    @Mock
    AuthUserBaseDao authUserDao;

    @Mock
    UserInfoBaseDao userInfoDao;

    @InjectMocks
    DefaultAuthUserService service;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Timestamp getTime() {
        String time = sdf.format(System.currentTimeMillis());
        return Timestamp.valueOf(time);
    }

    private AuthUserDto createTestAuthUser(String name) {
        return new AuthUserDto(name, name + "Pass");
    }

    private UserInfoDto createTestUserInfo() {


        return new UserInfoDto(getTime());
    }

    @Test
    void createAuthUserAndUserInfo() {
        final String testLogin = "TestLogin";
        final String testPassword = "TestLoginPass";
        final Long testId = 1L;

        final AuthUserDto newAuthUser = createTestAuthUser(testLogin);
        final AuthUserDto testAuthUser = createTestAuthUser(testLogin);
        testAuthUser.setId(testId);
        testAuthUser.setRole(Role.ADMIN);
        testAuthUser.setDeleted(true);

        when(authUserDao.add(newAuthUser)).thenReturn(testAuthUser);

        final UserInfoDto newUserInfo = createTestUserInfo();
        newUserInfo.setAuthId(testId);
        final UserInfoDto testUserInfo = createTestUserInfo();
        testUserInfo.setAuthId(testId);
        testUserInfo.setUserLogin(testLogin);

        when(userInfoDao.add(newUserInfo)).thenReturn(testUserInfo);

        final AuthUserDto userFromDB = service.createAuthUserAndUserInfo(new AuthUserDto(testLogin, testPassword));
        assertNotNull(userFromDB);
        assertEquals(testAuthUser, userFromDB);
        assertEquals(testUserInfo, userFromDB.getUserInfoDto());
    }

    @Test
    void loginNotExist() {
        final String testLogin = "loginNotExist";
        when(authUserDao.getByLogin(testLogin)).thenReturn(null);
        final AuthUserDto testUser = createTestAuthUser(testLogin);
        final AuthUserDto userFromDB = service.loginAuthUser(testUser);
        assertNull(userFromDB);
    }

    @Test
    void loginExistPassCorr() {
        final String testLogin = "loginExistPassCorr";
        final Long testId = 1L;
        final AuthUserDto userFromLogin = createTestAuthUser(testLogin);

        final AuthUserDto testAuthUser = createTestAuthUser(testLogin);
        testAuthUser.setId(testId);
        testAuthUser.setRole(Role.ADMIN);
        testAuthUser.setDeleted(true);

        final UserInfoDto testUserInfo = createTestUserInfo();
        testUserInfo.setAuthId(testId);
        testUserInfo.setUserLogin(testLogin);

        testAuthUser.setUserInfoDto(testUserInfo);

        when(authUserDao.getByLogin(testLogin)).thenReturn(testAuthUser);

        final AuthUserDto userFromDB = service.loginAuthUser(userFromLogin);
        assertNotNull(userFromDB);
        assertEquals(testAuthUser, userFromDB);
        assertEquals(testUserInfo, userFromDB.getUserInfoDto());
    }

    @Test
    void loginExistPassWrong() {
        final String testLogin = "loginExistPassWrong";
        final AuthUserDto userFromLogin = createTestAuthUser(testLogin);

        final AuthUserDto testAuthUser = createTestAuthUser(testLogin);
        testAuthUser.setPassword("WrongPassword");

        when(authUserDao.getByLogin(testLogin)).thenReturn(testAuthUser);
        final AuthUserDto userFromDB = service.loginAuthUser(userFromLogin);
        assertNull(userFromDB);
    }


}