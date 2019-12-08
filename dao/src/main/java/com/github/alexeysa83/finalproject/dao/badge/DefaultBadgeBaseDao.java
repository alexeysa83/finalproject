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

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public BadgeDto add(BadgeDto badgeDto) {
        final BadgeEntity badgeEntity = BadgeConvert.toEntity(badgeDto);
        final BadgeEntity savedToDB = badgeRepository.save(badgeEntity);
        if (savedToDB != null) {
            log.info("Badge id: {} saved to DB at: {}", savedToDB.getId(), LocalDateTime.now());
        } else {
            log.error("Fail to save new badge to DB: {}, at: {}", badgeDto, LocalDateTime.now());
        }
        return BadgeConvert.toDto(savedToDB);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean isNameTaken(String name) {
        final BadgeEntity badgeEntity = badgeRepository.findByBadgeName(name);
        return badgeEntity != null;
    }

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
        List<BadgeDto> badgeDtos;
        final List<BadgeEntity> listEntities = badgeRepository.findAll(Sort.by("badgeName"));
        badgeDtos = listEntities.stream().map(BadgeConvert::toDto).collect(Collectors.toList());
        return badgeDtos;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean update(BadgeDto badgeDto) {
        final int rowsUpdated = badgeRepository.updateBadgeName(badgeDto.getId(), badgeDto.getBadgeName());
        if (rowsUpdated > 0) {
            log.info("Badge id: {} updated in DB at: {}", badgeDto.getId(), LocalDateTime.now());
        } else {
            log.error("Fail to update in DB badge: {} at: {}", badgeDto, LocalDateTime.now());
        }
        return rowsUpdated > 0;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean delete(Long id) {

        final int rowsDeleted = badgeRepository.deleteBadge(id);
        if (rowsDeleted > 0) {
            log.info("Badge id: {} deleted from DB at: {}", id, LocalDateTime.now());
        } else {
            log.error("Fail to delete badge id from DB: {}, at: {}", id, LocalDateTime.now());
        }
        return rowsDeleted > 0;
    }
}
