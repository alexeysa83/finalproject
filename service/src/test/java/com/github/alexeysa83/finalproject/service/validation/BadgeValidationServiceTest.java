package com.github.alexeysa83.finalproject.service.validation;

import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BadgeValidationServiceTest {

    @Mock
    BadgeService badgeService;

    @InjectMocks
    BadgeValidationService validationService;

    @Test
    void validName() {
        final String testMessage = "validName";
        when(badgeService.checkNameIsTaken(testMessage)).thenReturn(false);
        final String returnMessage = validationService.isNameValid(testMessage);
        assertNull(returnMessage);
    }

    @Test
    void NameIsTaken() {
        final String testMessage = "NameIsTaken";
        when(badgeService.checkNameIsTaken(testMessage)).thenReturn(true);
        final String returnMessage = validationService.isNameValid(testMessage);
        assertEquals("name.istaken", returnMessage);
        }

    @Test
    void invalidName() {
        final String returnedMessage = validationService.isNameValid("");
        assertEquals("invalid.name", returnedMessage);
    }
}