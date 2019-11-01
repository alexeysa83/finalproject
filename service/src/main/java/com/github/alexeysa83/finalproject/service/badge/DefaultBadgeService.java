package com.github.alexeysa83.finalproject.service.badge;

import com.github.alexeysa83.finalproject.dao.badge.BadgeBaseDao;
import com.github.alexeysa83.finalproject.dao.badge.DefaultBadgeBaseDao;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;

import java.util.Set;

public class DefaultBadgeService implements BadgeService {

    private BadgeBaseDao badgeDao = DefaultBadgeBaseDao.getInstance();

    private static volatile BadgeService instance;

        public static BadgeService getInstance() {
            BadgeService localInstance = instance;
            if (localInstance == null) {
                synchronized (BadgeService.class) {
                    localInstance = instance;
                    if (localInstance == null) {
                        instance = localInstance = new DefaultBadgeService();
                    }
                }
            }
            return localInstance;
        }

    @Override
    public BadgeDto addNewBadge(BadgeDto badgeDto) {
        return badgeDao.add(badgeDto);
    }

    @Override
    public BadgeDto getById(long id) {
        return badgeDao.getById(id);
    }

    @Override
    public Set<BadgeDto> getAllBadges() {
        return badgeDao.getAll();
    }

    @Override
    public boolean update(BadgeDto badgeDto) {
        return badgeDao.update(badgeDto);
    }

    @Override
    public boolean deleteBadge(long id) {
        return badgeDao.delete(id);
    }
}
