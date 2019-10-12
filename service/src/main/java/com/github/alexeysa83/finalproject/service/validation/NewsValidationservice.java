package com.github.alexeysa83.finalproject.service.validation;

public abstract class NewsValidationservice {

    private NewsValidationservice() {

    }

       // ++ Regex pattern
    public static String isValidTitleContent(String title, String content) {
        String message = null;
        if ((title.length() < 1) || (content.length() < 1)) {
            message = "Title or content is not completed";
        }
        return message;
    }
}
