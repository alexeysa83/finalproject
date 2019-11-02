package com.github.alexeysa83.finalproject.service.auth;

import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.model.Role;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultSecurityServiceTest {

    @Mock
    AuthUserBaseDao authUserDao;

    @InjectMocks
    DefaultSecurityService service;

    private AuthUserDto createTestAuthUser(String name) {
        UserInfoDto userInfoDto = new UserInfoDto(new Timestamp(System.currentTimeMillis()));
        return new AuthUserDto(name, name + "Pass", userInfoDto);
    }

    @Test
    void getInstance() {
        SecurityService securityService = DefaultSecurityService.getInstance();
        assertNotNull(securityService);
    }

    @Test
    void createAndSaveAuthUser() {
        final String testLogin = "TestLogin";
        final AuthUserDto userFromLogin = createTestAuthUser(testLogin);

        final AuthUserDto testAuthUser = new AuthUserDto
                (testLogin, userFromLogin.getPassword(), userFromLogin.getUserInfoDto());
        testAuthUser.setId(1);
        testAuthUser.setRole(Role.USER);
        testAuthUser.setDeleted(false);
        testAuthUser.getUserInfoDto().setAuthId(1);

        when(authUserDao.add(userFromLogin)).thenReturn(testAuthUser);
        final AuthUserDto userFromDB = service.createAuthUser(testLogin, userFromLogin.getPassword());

        assertNotNull(userFromDB);
        assertEquals(testAuthUser, userFromDB);
        assertEquals(testAuthUser.getUserInfoDto(), userFromDB.getUserInfoDto());
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
        final AuthUserDto userFromLogin = createTestAuthUser(testLogin);

        final AuthUserDto testAuthUser = new AuthUserDto
                (testLogin, userFromLogin.getPassword(), userFromLogin.getUserInfoDto());
        testAuthUser.setId(1);
        testAuthUser.setRole(Role.USER);
        testAuthUser.setDeleted(false);
        testAuthUser.getUserInfoDto().setAuthId(1);

        when(authUserDao.getByLogin(testLogin)).thenReturn(testAuthUser);
        final AuthUserDto userFromDB = service.loginAuthUser(userFromLogin);
        assertNotNull(userFromDB);
        assertEquals(testAuthUser.getUserInfoDto(), userFromDB.getUserInfoDto());
        assertEquals(testAuthUser.getUserInfoDto(), userFromDB.getUserInfoDto());
    }

    @Test
    void loginExistPassWrong() {
        final String testLogin = "loginExistPassWrong";
        final AuthUserDto userFromLogin = createTestAuthUser(testLogin);

        final AuthUserDto testAuthUser = new AuthUserDto
                (testLogin, userFromLogin.getPassword(), userFromLogin.getUserInfoDto());
        testAuthUser.setPassword("WrongPassword");

        when(authUserDao.getByLogin(testLogin)).thenReturn(testAuthUser);
        final AuthUserDto userFromDB = service.loginAuthUser(userFromLogin);
        assertNull(userFromDB);
    }
}