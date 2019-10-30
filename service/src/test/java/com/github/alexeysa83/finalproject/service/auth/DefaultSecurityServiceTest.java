package com.github.alexeysa83.finalproject.service.auth;

import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.model.Role;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultSecurityServiceTest {

    @Mock
    AuthUserBaseDao authUserDao;

    @InjectMocks
    DefaultSecurityService service;

    @Test
    void createAndSaveAuthUser() {
        final String testLogin = "TestLogin";
        final String testPassword = "TestPass";
        final AuthUserDto testAuthUser = new AuthUserDto
                (1, testLogin, testPassword, Role.USER,
                        false, new UserDto(null));

        when(authUserDao.createAndSave(any())).thenReturn(testAuthUser);
        final AuthUserDto userFromDB = service.createAndSaveAuthUser(testLogin, testPassword);

        assertNotNull(userFromDB);
        assertEquals(testAuthUser.getId(), userFromDB.getId());
        assertEquals(testLogin, userFromDB.getLogin());
        assertEquals(testPassword, userFromDB.getPassword());
        assertEquals(testAuthUser.getRole(), userFromDB.getRole());
        assertEquals(testAuthUser.isBlocked(), userFromDB.isBlocked());
        assertEquals(testAuthUser.getUserDto(), userFromDB.getUserDto());
    }

    // if needed
    private AuthUserDto createTestAuthUser(String name) {
        UserDto userDto = new UserDto(new Timestamp(System.currentTimeMillis()));
        return new AuthUserDto(name, name + "Pass", userDto);
    }

//    @Test
//    void getById() {
//    }

//    @Test
//    void loginNotExist() {
//        when(authUserDao.getByLogin("loginNotExist")).thenReturn(null);
//        AuthUserDto testUser = new AuthUserDto("loginNotExist", "Pass");
//        AuthUserDto userFromDB = service.login(testUser);
//        assertNull(userFromDB);
//    }
//
//    @Test
//    void loginExistPassCorr() {
//        when(authUserDao.getByLogin("loginExist"))
//                .thenReturn(new AuthUserDto("loginExist", "passCorrect"));
//        AuthUserDto testUser = new AuthUserDto("loginExist", "passCorrect");
//        AuthUserDto userFromDB = service.login(testUser);
//        assertNotNull(userFromDB);
//        assertEquals(testUser.getLogin(), userFromDB.getLogin());
//        assertEquals(testUser.getPassword(), userFromDB.getPassword());
//    }
//
//    @Test
//    void loginExistPassWrong() {
//        when(authUserDao.getByLogin("loginExist"))
//                .thenReturn(new AuthUserDto("loginExist", "passCorrect"));
//        AuthUserDto testUser = new AuthUserDto("loginExist", "passWrong");
//        AuthUserDto userFromDB = service.login(testUser);
//        assertNull(userFromDB);
//    }

//    @Test
//    void checkLoginIsTaken() {
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void delete() {
//    }
}