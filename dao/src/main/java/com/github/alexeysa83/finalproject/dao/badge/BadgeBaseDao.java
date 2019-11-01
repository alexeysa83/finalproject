package com.github.alexeysa83.finalproject.dao.badge;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;

import java.util.Set;

public interface BadgeBaseDao extends BaseDao <BadgeDto> {

    BadgeDto addBadge(BadgeDto badge);

    Set<BadgeDto> getAllBadges ();

    boolean deleteBadge(long id);
}
