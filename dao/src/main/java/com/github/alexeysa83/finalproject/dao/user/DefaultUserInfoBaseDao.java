package com.github.alexeysa83.finalproject.dao.user;

import com.github.alexeysa83.finalproject.dao.convert_entity.UserInfoConvert;
import com.github.alexeysa83.finalproject.dao.entity.BadgeEntity;
import com.github.alexeysa83.finalproject.dao.entity.UserInfoEntity;
import com.github.alexeysa83.finalproject.dao.repository.UserInfoRepository;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public class DefaultUserInfoBaseDao implements UserInfoBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserInfoBaseDao.class);

    private final UserInfoRepository userInfoRepository;

    public DefaultUserInfoBaseDao(UserInfoRepository userInfoRepository) {
                this.userInfoRepository = userInfoRepository;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public UserInfoDto add(UserInfoDto userInfoDto) {
        final Long authId = userInfoDto.getAuthId();
        userInfoRepository.saveUserInfo(authId, userInfoDto.getRegistrationTime());
        final UserInfoEntity savedUserEntity = userInfoRepository.getOne(authId);

        if (savedUserEntity != null) {
            log.info("UserInfo authid: {} saved to DB at: {}", authId, LocalDateTime.now());
        } else {
            log.error("Fail to save new userInfo to DB: {}, at: {}", userInfoDto, LocalDateTime.now());
        }
        return UserInfoConvert.toDto(savedUserEntity);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserInfoDto getById(Long authId) {
        final Optional<UserInfoEntity> optional = userInfoRepository.findById(authId);
        if (optional.isPresent()) {
            final UserInfoEntity userInfoEntity = optional.get();
            return UserInfoConvert.toDto(userInfoEntity);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean update(UserInfoDto userInfoDto) {
        final int rowsUpdated = userInfoRepository.updateUserInfo(
                userInfoDto.getAuthId(),
                userInfoDto.getFirstName(),
                userInfoDto.getLastName(),
                userInfoDto.getEmail(),
                userInfoDto.getPhone());

        if (rowsUpdated > 0) {
            log.info("UserInfo id: {} updated in DB at: {}", userInfoDto.getAuthId(), LocalDateTime.now());
        } else {
            log.error("Fail to update in DB UserInfo: {} at: {}", userInfoDto, LocalDateTime.now());
        }
        return rowsUpdated > 0;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean delete(Long id) {
        final int rowsUpdated = userInfoRepository.deleteUserInfo(id);

        if (rowsUpdated > 0) {
            log.info("UserInfo id: {} deleted from DB at: {}", id, LocalDateTime.now());
        } else {
            log.error("Fail to delete UserInfo: {} at: {}", id, LocalDateTime.now());
        }
        return rowsUpdated > 0;
    }


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public UserInfoDto addBadgeToUser(Long authId, Long badgeId) {

        final UserInfoEntity userInfoEntity = userInfoRepository.getOne(authId);
        final BadgeEntity badgeEntity = userInfoRepository.getBadgeById(badgeId);
        if (badgeEntity != null) {
            userInfoEntity.addBadge(badgeEntity);
            userInfoRepository.flush();
            log.info("Badge id: {} added to user id {} in DB at: {}", badgeId, authId, LocalDateTime.now());
        } else {
            log.error("Fail to add badge id: {} to user id {} in DB at: {}. Badge not found in DB",
                    badgeId, authId, LocalDateTime.now());
        }
        return UserInfoConvert.toDto(userInfoEntity);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public UserInfoDto deleteBadgeFromUser(Long authId, Long badgeId) {

        final UserInfoEntity userInfoEntity = userInfoRepository.getOne(authId);
        final BadgeEntity badgeEntity = userInfoRepository.getBadgeById(badgeId);
        if (badgeEntity != null) {
            userInfoEntity.deleteBadge(badgeEntity);
            userInfoRepository.flush();
            log.info("Badge id: {} deleted from user id {} in DB at: {}", badgeId, authId, LocalDateTime.now());
        } else {
            log.info("Fail to delete badge id: {} from user id {} in DB at: {}. Badge not found in DB",
                    badgeId, authId, LocalDateTime.now());
        }
        return UserInfoConvert.toDto(userInfoEntity);
    }
}
