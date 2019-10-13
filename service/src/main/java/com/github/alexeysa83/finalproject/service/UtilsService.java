package com.github.alexeysa83.finalproject.service;

public abstract class UtilsService {

    private UtilsService() {
    }

    public static long stringToLong (String value) {
        return Long.parseLong(value);
    }
   }
