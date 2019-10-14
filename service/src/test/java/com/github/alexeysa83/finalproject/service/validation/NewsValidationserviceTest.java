package com.github.alexeysa83.finalproject.service.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NewsValidationserviceTest {

    @Test
    void isValidTitleContent() {
        String message = NewsValidationservice.isValidTitleContent("", "Content");
        assertEquals("invalid.news", message);

        message = NewsValidationservice.isValidTitleContent("Title", "");
        assertEquals("invalid.news", message);

        message = NewsValidationservice.isValidTitleContent("Title", "Content");
        assertNull(message);
    }
}