package com.github.alexeysa83.finalproject.service.validation;

import com.github.alexeysa83.finalproject.model.AuthUser;
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

//    @InjectMocks
//    AuthValidationService validationService;

//    @Test
//    void isLoginValid() {
//        // Check valid login
//        when(securityService.checkLoginIsTaken("invalidLogin")).thenReturn(true);
//        String message = AuthValidationService.isLoginValid("validLogin");
//        assertNull(message);
//
//        // Check "empty" login
//        message = AuthValidationService.isLoginValid("");
//        assertEquals("Unacceptable login", message);
//
//        //Check login is already taken
//        message = AuthValidationService.isLoginValid("invalidLogin");
//        assertEquals("Login is already taken", message);
//    }

    @Test
    void isPasswordValid() {
        // Check valid password
        String message = AuthValidationService.isPasswordValid("Pass", "Pass");
        assertNull(message);

        // Check "empty" password
        message = AuthValidationService.isPasswordValid("", "Pass");
        assertEquals("Unacceptable password", message);

        //Check password do not match
        message = AuthValidationService.isPasswordValid("Pass", "Sapp");
        assertEquals("Repeat password do not match", message);
    }

//    @Test
//    void isPasswordEqual() {
//
//    }

    @Test
    void needLogout() {
        AuthUser testUser = new AuthUser(1, "Test", "Test", null, false);
        boolean result = AuthValidationService.needLogout(testUser, "1");
        assertTrue(result);

        result = AuthValidationService.needLogout(testUser, "2");
        assertFalse(result);
    }

    @Test
    void isRoleValid() {
        boolean result = AuthValidationService.isRoleValid("USER");
        assertTrue(result);

        result = AuthValidationService.isRoleValid("RESU");
        assertFalse(result);
    }

//    @Test
//    void isAdmin() {
//    }
}