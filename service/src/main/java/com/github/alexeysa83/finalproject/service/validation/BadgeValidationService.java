package com.github.alexeysa83.finalproject.service.validation;

import com.github.alexeysa83.finalproject.service.badge.BadgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BadgeValidationService {

    @Autowired
    private BadgeService badgeService;

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
