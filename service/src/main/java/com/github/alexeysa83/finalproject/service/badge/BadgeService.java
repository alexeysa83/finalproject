package com.github.alexeysa83.finalproject.service.badge;

import com.github.alexeysa83.finalproject.model.dto.BadgeDto;

import java.util.List;

public interface BadgeService {

    BadgeDto createBadge(BadgeDto badgeDto);

    List<BadgeDto> getAllBadges ();

    boolean checkNameIsTaken (String name);

    boolean updateBadge(BadgeDto badgeDto);

    boolean deleteBadge (Long id);
}
