package com.github.alexeysa83.finalproject.service.validation;

import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import com.github.alexeysa83.finalproject.service.badge.DefaultBadgeService;

public class BadgeValidationservice {

    private BadgeService badgeService = DefaultBadgeService.getInstance();

    public String isNameValid (String name) {
        String message = null;
        if (name.length() <1) {
            message = "invalid.name";
            } else if (badgeService.checkNameIsTaken(name)) {
            message = "name.istaken";
        }
        return message;
    }


}
