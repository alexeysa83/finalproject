package com.github.alexeysa83.finalproject.service.badge;

import com.github.alexeysa83.finalproject.model.dto.BadgeDto;

import java.util.Set;

public interface BadgeService {

    BadgeDto addNewBadge (BadgeDto badgeDto);

    BadgeDto getById (long id);

    Set<BadgeDto> getAllBadges ();

    boolean update (BadgeDto badgeDto);

    boolean deleteBadge (long id);
}
