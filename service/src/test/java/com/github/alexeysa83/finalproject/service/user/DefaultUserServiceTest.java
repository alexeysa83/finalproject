package com.github.alexeysa83.finalproject.service.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultUserServiceTest {

    @Test
    void getInstance() {
        UserService userService = DefaultUserService.getInstance();
        assertNotNull(userService);
    }
}