package com.github.alexeysa83.finalproject.dao.badge;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;

import java.util.List;

public interface BadgeBaseDao extends BaseDao<BadgeDto> {

    boolean isNameTaken(String name);

    List<BadgeDto> getAll();
}
