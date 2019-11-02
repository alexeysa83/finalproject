package com.github.alexeysa83.finalproject.service.validation;

public abstract class NewsValidationService {

    private NewsValidationService() {
    }

       // ++ Regex pattern
    public static String isValidTitleContent(String title, String content) {
        String message = null;
        if ((title.length() < 1) || (content.length() < 1)) {
            message = "invalid.news";
        }
        return message;
    }
}
