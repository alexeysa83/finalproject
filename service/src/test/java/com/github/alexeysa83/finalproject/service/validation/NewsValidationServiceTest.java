package com.github.alexeysa83.finalproject.service.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NewsValidationServiceTest {

    @Test
    void notValidTitle() {
        final String returnedMessage = NewsValidationService.isValidTitleContent("", "Content");
        assertEquals("invalid.news", returnedMessage);
    }

    @Test
    void notValidContent() {
        final String returnedMessage = NewsValidationService.isValidTitleContent("Title", "");
        assertEquals("invalid.news", returnedMessage);
    }

    @Test
    void validTitleContent() {
        final String message = NewsValidationService.isValidTitleContent("Title", "Content");
        assertNull(message);
    }
}