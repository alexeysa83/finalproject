package com.github.alexeysa83.finalproject.service.validation;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.service.auth.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthValidationServiceTest {

    @Mock
    SecurityService securityService;

    @InjectMocks
    AuthValidationService validationService;

    @Test
    void validLogin() {
        when(securityService.checkLoginIsTaken("validLogin")).thenReturn(false);
        String message = validationService.isLoginValid("validLogin");
        assertNull(message);
    }

    @Test
    void loginIsTaken() {
        when(securityService.checkLoginIsTaken("LoginIsTaken")).thenReturn(true);
        String message = validationService.isLoginValid("LoginIsTaken");
        assertEquals("login.istaken", message);
    }

    @Test
    void invalidLogin() {
        String message = validationService.isLoginValid("");
        assertEquals("invalid.login", message);
    }



    @Test
    void isPasswordValid() {
        // Check valid password
        String message = validationService.isPasswordValid("Pass", "Pass");
        assertNull(message);

        // Check "empty" password
        message = validationService.isPasswordValid("", "Pass");
        assertEquals("invalid.pass", message);

        //Check password do not match
        message = validationService.isPasswordValid("Pass", "Sapp");
        assertEquals("invalid.repeatpass", message);
    }

//    @Test
//    void isPasswordEqual() {
//
//    }

    @Test
    void needLogout() {
        AuthUser testUser = new AuthUser(1, "Test", "Test", null, false);
        boolean result = validationService.needLogout(testUser, "1");
        assertTrue(result);

        result = validationService.needLogout(testUser, "2");
        assertFalse(result);
    }

    @Test
    void isRoleValid() {
        boolean result = validationService.isRoleValid("USER");
        assertTrue(result);

        result = validationService.isRoleValid("RESU");
        assertFalse(result);
    }

//    @Test
//    void isAdmin() {
//    }
}