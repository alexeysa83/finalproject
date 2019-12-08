package com.github.alexeysa83.finalproject.service.badge;

import com.github.alexeysa83.finalproject.dao.badge.BadgeBaseDao;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultBadgeService implements BadgeService {

    private final BadgeBaseDao badgeDao;

    public DefaultBadgeService(BadgeBaseDao badgeDao) {
        this.badgeDao = badgeDao;
    }

    @Override
    public BadgeDto createBadge(BadgeDto badgeDto) {
        return badgeDao.add(badgeDto);
    }

    @Override
    public List<BadgeDto> getAllBadges() {
        return badgeDao.getAll();
    }

    @Override
    public boolean checkNameIsTaken(String name) {
        return badgeDao.isNameTaken(name);
    }

    @Override
    public boolean updateBadge(BadgeDto badgeDto) {
        return badgeDao.update(badgeDto);
    }

    @Override
    public boolean deleteBadge(Long id) {
        return badgeDao.delete(id);
    }
}
