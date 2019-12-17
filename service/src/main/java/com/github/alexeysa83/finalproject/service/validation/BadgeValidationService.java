package com.github.alexeysa83.finalproject.service.validation;

import com.github.alexeysa83.finalproject.service.badge.BadgeService;

public class BadgeValidationService {

      private final BadgeService badgeService;

    public BadgeValidationService(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    /**
     *
     * @param name
     * @return
     */
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
