package com.github.alexeysa83.finalproject.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public abstract class UtilService {

    private UtilService() {
    }

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Testing?
     */
    public static Timestamp getTime() {
        String time = sdf.format(System.currentTimeMillis());
        return Timestamp.valueOf(time);
    }
}
