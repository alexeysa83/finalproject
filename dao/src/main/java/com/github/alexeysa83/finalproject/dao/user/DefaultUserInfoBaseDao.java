package com.github.alexeysa83.finalproject.dao.user;

import com.github.alexeysa83.finalproject.dao.SessionManager;
import com.github.alexeysa83.finalproject.dao.convert_entity.UserInfoConvert;
import com.github.alexeysa83.finalproject.dao.entity.BadgeEntity;
import com.github.alexeysa83.finalproject.dao.entity.UserInfoEntity;
import com.github.alexeysa83.finalproject.dao.repository.UserInfoRepository;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class DefaultUserInfoBaseDao extends SessionManager implements UserInfoBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserInfoBaseDao.class);

    private final UserInfoRepository userInfoRepository;

    public DefaultUserInfoBaseDao(UserInfoRepository userInfoRepository, SessionFactory factory) {
        super(factory);
        this.userInfoRepository = userInfoRepository;
    }

    /**
     *UserInfoDto is already with authid
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public UserInfoDto add(UserInfoDto userInfoDto) {
        final Long authId = userInfoDto.getAuthId();
        UserInfoDto savedUser;
        try {
//            final UserInfoEntity save = userInfoRepository.save(userInfoEntity);
            userInfoRepository.saveUserInfo(authId, userInfoDto.getRegistrationTime());
            final UserInfoEntity savedUserEntity = userInfoRepository.getOne(authId);
            savedUser = UserInfoConvert.toDto(savedUserEntity);
            //        } catch (PersistenceException | IllegalArgumentException e) {
        } catch (RuntimeException e) {
            log.error("Fail to save new userInfo to DB: {}, at: {}", userInfoDto, LocalDateTime.now(), e);
//            throw new RuntimeException(e);
            return null;
        }
        log.info("UserInfo authid: {} saved to DB at: {}", authId, LocalDateTime.now());
        return savedUser;
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
        String message = "UserInfo id: {} updated in DB at: {}";

        try {
            final int rowsUpdated = userInfoRepository.updateUserInfo(
                    userInfoDto.getAuthId(),
                    userInfoDto.getFirstName(),
                    userInfoDto.getLastName(),
                    userInfoDto.getEmail(),
                    userInfoDto.getPhone());

            if (rowsUpdated <= 0 ) {
                message = "Fail to update in DB UserInfo: {} at: {}";
            }
            log.info(message, userInfoDto.getAuthId(), LocalDateTime.now());
            return rowsUpdated > 0;
        } catch (PersistenceException e) {
            log.error("Fail to update in DB UserInfo: {} at: {}", userInfoDto, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean delete(Long id) {
        String message = "UserInfo id: {} deleted from DB at: {}";

        try {
            final int rowsUpdated = userInfoRepository.deleteUserInfo(id);

            if (rowsUpdated <= 0 ) {
                message = "Fail to delete UserInfo: {} at: {}";
            }
            log.info(message, id, LocalDateTime.now());
            return rowsUpdated > 0;
        } catch (PersistenceException e) {
            log.error("Fail to delete UserInfo: {} at: {}", id, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Spring data???
          */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public UserInfoDto addBadgeToUser(Long authId, Long badgeId) {

        try  {
            Session session = getSession();
            final UserInfoEntity userInfoEntity = session.get(UserInfoEntity.class, authId);
            final BadgeEntity badgeEntity = session.get(BadgeEntity.class, badgeId);
            userInfoEntity.addBadge(badgeEntity);
            session.update(userInfoEntity);
            log.info("Badge id: {} added to user id {} in DB at: {}", badgeId, authId, LocalDateTime.now());
            return UserInfoConvert.toDto(userInfoEntity);
        } catch (PersistenceException e) {
            log.info("Fail to add badge id: {} to user id {} in DB at: {}", badgeId, authId, LocalDateTime.now());
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public UserInfoDto deleteBadgeFromUser(Long authId, Long badgeId) {

        try  {
            Session session = getSession();
            final UserInfoEntity userInfoEntity = session.get(UserInfoEntity.class, authId);
            final BadgeEntity badgeEntity = session.get(BadgeEntity.class, badgeId);
            userInfoEntity.deleteBadge(badgeEntity);
            session.update(userInfoEntity);
            log.info("Badge id: {} deleted from user id {} in DB at: {}", badgeId, authId, LocalDateTime.now());
            return UserInfoConvert.toDto(userInfoEntity);
        } catch (PersistenceException e) {
            log.info("Fail to delete badge id: {} from user id {} in DB at: {}", badgeId, authId, LocalDateTime.now());
            throw new RuntimeException(e);
        }
    }
}
