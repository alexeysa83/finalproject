package com.github.alexeysa83.finalproject.dao.badge;

import com.github.alexeysa83.finalproject.dao.convert_entity.BadgeConvert;
import com.github.alexeysa83.finalproject.dao.entity.BadgeEntity;
import com.github.alexeysa83.finalproject.dao.repository.BadgeRepository;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class DefaultBadgeBaseDao implements BadgeBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultBadgeBaseDao.class);

    private final BadgeRepository badgeRepository;

    public DefaultBadgeBaseDao(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    /**
     * ??
         */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public BadgeDto add(BadgeDto badgeDto) {
        final BadgeEntity badgeEntity = BadgeConvert.toEntity(badgeDto);
        BadgeEntity savedToDB;
        try {
            savedToDB = badgeRepository.save(badgeEntity);
//        } catch (PersistenceException | IllegalArgumentException e) {
        } catch (RuntimeException e) {
            log.error("Fail to save new badge to DB: {}, at: {}", badgeDto, LocalDateTime.now(), e);
//            throw new RuntimeException(e);
            return null;
        }
        log.info("Badge id: {} saved to DB at: {}", savedToDB.getId(), LocalDateTime.now());
        return BadgeConvert.toDto(savedToDB);
    }

    /**
     * Need try/catch?
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean isNameTaken(String name) {
        final BadgeEntity badgeEntity = badgeRepository.findByBadgeName(name);
        return badgeEntity != null;
    }

    /**
     * Used only in tests
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public BadgeDto getById(Long id) {
        final Optional<BadgeEntity> optional = badgeRepository.findById(id);
        if (optional.isPresent()) {
            final BadgeEntity badgeEntity = optional.get();
            return BadgeConvert.toDto(badgeEntity);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<BadgeDto> getAll() {
        try {
            List<BadgeDto> badgeDtos;
            final List<BadgeEntity> listEntities = badgeRepository.findAll(Sort.by("badgeName"));
            badgeDtos = listEntities.stream().map(BadgeConvert::toDto).collect(Collectors.toList());
            return badgeDtos;
        } catch (PersistenceException e) {
            log.error("Fail to get list of badges from DB at: {}", LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Optimization
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean update(BadgeDto badgeDto) {
        String message = "Badge id: {} updated in DB at: {}";
        try {
            final int rowsUpdated = badgeRepository.updateBadgeName(badgeDto.getId(), badgeDto.getBadgeName());
            if (rowsUpdated <= 0) {
                message = "Fail to update in DB badge: {} at: {}";
            }
            log.info(message, badgeDto.getId(), LocalDateTime.now());
            return rowsUpdated > 0;
        } catch (PersistenceException e) {
            log.error("Fail to update in DB badge: {} at: {}", badgeDto, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean delete(Long id) {
        String message = "Badge id: {} deleted from DB at: {}";
        try {
            final int rowsDeleted = badgeRepository.deleteBadge(id);
            if (rowsDeleted <= 0) {
                message = "Fail to delete badge id from DB: {}, at: {}";
            }
            log.info(message, id, LocalDateTime.now());
            return rowsDeleted > 0;
        } catch (PersistenceException e) {
            log.error("Fail to delete badge id from DB: {}, at: {}", id, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }
}
