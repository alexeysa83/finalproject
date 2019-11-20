package com.github.alexeysa83.finalproject.service.badge;

import com.github.alexeysa83.finalproject.dao.badge.BadgeBaseDao;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultBadgeService implements BadgeService {

    @Autowired
    private BadgeBaseDao badgeDao;

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
    public boolean deleteBadge(long id) {
        return badgeDao.delete(id);
    }
}
