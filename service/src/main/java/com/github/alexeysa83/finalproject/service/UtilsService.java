package com.github.alexeysa83.finalproject.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

// getTime to different class???
public abstract class UtilsService {

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private UtilsService() {
    }

    public static long stringToLong (String value) {
        return Long.parseLong(value);
    }

    public static Timestamp getTime() {
        String time = sdf.format(System.currentTimeMillis());
        return Timestamp.valueOf(time);
    }
}
