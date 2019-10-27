package com.github.alexeysa83.finalproject.service.validation;

public abstract class CommentValidationService {

    private CommentValidationService(){
            }

    public static String isValidContent(String content) {
        String message = null;
        if (content.length() < 1) {
            message = "invalid.message";
        }
        return message;
    }
}
