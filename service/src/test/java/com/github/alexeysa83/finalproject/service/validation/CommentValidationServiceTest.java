package com.github.alexeysa83.finalproject.service.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentValidationServiceTest {

    @Test
    void notValidContent() {
        final String returnedMessage = CommentValidationService.isValidContent( "");
        assertEquals("invalid.comment", returnedMessage);
    }

    @Test
    void validTitleContent() {
        final String returnedMessage = CommentValidationService.isValidContent("Content");
        assertNull(returnedMessage);
    }
}