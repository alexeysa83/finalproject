package com.github.alexeysa83.finalproject.service.badge;

import com.github.alexeysa83.finalproject.dao.badge.BadgeBaseDao;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class DefaultBadgeService implements BadgeService {

    private final BadgeBaseDao badgeDao;

    public DefaultBadgeService(BadgeBaseDao badgeDao) {
        this.badgeDao = badgeDao;
    }

    @Override
    @Transactional
    public BadgeDto createBadge(BadgeDto badgeDto) {
        return badgeDao.add(badgeDto);
    }

    @Override
    @Transactional
    public List<BadgeDto> getAllBadges() {
        return badgeDao.getAll();
    }

    @Override
    @Transactional
    public boolean checkNameIsTaken(String name) {
        return badgeDao.isNameTaken(name);
    }

    @Override
    @Transactional
    public boolean updateBadge(BadgeDto badgeDto) {
        return badgeDao.update(badgeDto);
    }

    @Override
    @Transactional
    public boolean deleteBadge(Long id) {
        return badgeDao.delete(id);
    }
}
