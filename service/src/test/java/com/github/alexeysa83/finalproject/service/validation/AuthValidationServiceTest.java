package com.github.alexeysa83.finalproject.service.validation;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthValidationServiceTest {

    @Mock
    SecurityService securityService;

    @InjectMocks
    AuthValidationService validationService;

    @Test
    void validLogin() {
        final String testMessage = "validLogin";
        when(securityService.checkLoginIsTaken(testMessage)).thenReturn(false);
        final String returnMessage = validationService.isLoginValid(testMessage);
        assertNull(returnMessage);
    }

    @Test
    void loginIsTaken() {
        final String testMessage = "LoginIsTaken";
        when(securityService.checkLoginIsTaken(testMessage)).thenReturn(true);
        final String returmMessage = validationService.isLoginValid(testMessage);
        assertEquals("login.istaken", returmMessage);
    }

    @Test
    void invalidLogin() {
        final String returnMessage = validationService.isLoginValid("");
        assertEquals("invalid.login", returnMessage);
    }

    @Test
    void passwordValid() {
        final String returnMessage = validationService.isPasswordValid("Pass", "Pass");
        assertNull(returnMessage);
    }

    @Test
    void passwordEmptyEntry() {
        final String returnMessage = validationService.isPasswordValid("", "Pass");
        assertEquals("invalid.pass", returnMessage);
    }

    @Test
    void passwordMismatch() {
        final String returnMessage = validationService.isPasswordValid("Pass", "Sapp");
        assertEquals("invalid.repeatpass", returnMessage);
    }

    @Test
    void needLogoutTrue() {
        final int testId = 1;
        final AuthUserDto testUser = new AuthUserDto();
        testUser.setId(testId);
        boolean result = validationService.needLogout(testUser, Integer.toString(testId));
        assertTrue(result);
    }

    @Test
    void needLogoutFalse() {
        final int testId = 1;
        final AuthUserDto testUser = new AuthUserDto();
        testUser.setId(testId);
        boolean result = validationService.needLogout(testUser, "2");
        assertFalse(result);
    }

    @Test
    void RoleValidTrue() {
        boolean result = validationService.isRoleValid("USER");
        assertTrue(result);
    }

    @Test
    void RoleValidFalse() {
        boolean result = validationService.isRoleValid("RESU");
        assertFalse(result);
    }
}