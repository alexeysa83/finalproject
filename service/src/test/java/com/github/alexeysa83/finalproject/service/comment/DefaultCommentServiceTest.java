package com.github.alexeysa83.finalproject.service.comment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultCommentServiceTest {

    @Test
    void getInstance() {
        CommentService commentService = DefaultCommentService.getInstance();
        assertNotNull(commentService);
    }
}