package com.github.alexeysa83.finalproject.dao.badge;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;

import java.util.Set;

public interface BadgeBaseDao extends BaseDao <BadgeDto> {

    BadgeDto add(BadgeDto badge);

    Set<BadgeDto> getAll();

    boolean delete(long id);
}
