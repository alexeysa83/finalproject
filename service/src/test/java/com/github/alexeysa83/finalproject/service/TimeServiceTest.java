package com.github.alexeysa83.finalproject.service;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class TimeServiceTest {

    @Test
    void getTime() {
        final Timestamp time = TimeService.getTime();
        assertNotNull(time);
    }
}