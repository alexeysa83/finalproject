package com.github.alexeysa83.finalproject.service.badge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultBadgeServiceTest {

    @Test
    void getInstance() {
        BadgeService badgeService = DefaultBadgeService.getInstance();
        assertNotNull(badgeService);
    }
}