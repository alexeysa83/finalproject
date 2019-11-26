package com.github.alexeysa83.finalproject.dao.user;

import com.github.alexeysa83.finalproject.dao.SessionManager;
import com.github.alexeysa83.finalproject.dao.convert_entity.UserInfoConvert;
import com.github.alexeysa83.finalproject.dao.entity.BadgeEntity;
import com.github.alexeysa83.finalproject.dao.entity.UserInfoEntity;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;

@Repository
public class DefaultUserInfoBaseDao extends SessionManager implements UserInfoBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserInfoBaseDao.class);

    public DefaultUserInfoBaseDao(SessionFactory factory) {
        super(factory);
    }

    @Override
    public UserInfoDto getById(long authId) {
        Session session = getSession();
        session.beginTransaction();
        final UserInfoEntity userInfoEntity = session.get(UserInfoEntity.class, authId);
        session.getTransaction().commit();
        final UserInfoDto userInfoDto = UserInfoConvert.toDto(userInfoEntity);
        session.close();
        return userInfoDto;
    }

    @Override
    public boolean update(UserInfoDto userInfoDto) {
        try (Session session = getSession()) {
            session.beginTransaction();

            final int i = session.createQuery
                    ("update UserInfoEntity u set u.firstName=:firstName, u.lastName=:lastName, " +
                            "u.email=:email, u.phone=:phone where u.authId=:authId")
                    .setParameter("firstName", userInfoDto.getFirstName())
                    .setParameter("lastName", userInfoDto.getLastName())
                    .setParameter("email", userInfoDto.getEmail())
                    .setParameter("phone", userInfoDto.getPhone())
                    .setParameter("authId", userInfoDto.getAuthId())
                    .executeUpdate();
            session.getTransaction().commit();
            log.info("User id: {} updated in DB at: {}", userInfoDto.getAuthId(), LocalDateTime.now());
            return i > 0;
        } catch (PersistenceException e) {
            log.error("Fail to update in DB User: {} at: {}", userInfoDto, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserInfoDto addBadgeToUser(long authId, long badgeId) {

        try (Session session = getSession()) {
            session.beginTransaction();
            final UserInfoEntity userInfoEntity = session.get(UserInfoEntity.class, authId);
            final BadgeEntity badgeEntity = session.get(BadgeEntity.class, badgeId);
            userInfoEntity.addBadge(badgeEntity);
            session.update(userInfoEntity);
            session.getTransaction().commit();
            log.info("Badge id: {} added to user id {} in DB at: {}", badgeId, authId, LocalDateTime.now());
            return UserInfoConvert.toDto(userInfoEntity);
        } catch (PersistenceException e) {
            log.info("Fail to add badge id: {} to user id {} in DB at: {}", badgeId, authId, LocalDateTime.now());
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserInfoDto deleteBadgeFromUser(long authId, long badgeId) {

        try (Session session = getSession()) {
            session.beginTransaction();
            final UserInfoEntity userInfoEntity = session.get(UserInfoEntity.class, authId);
            final BadgeEntity badgeEntity = session.get(BadgeEntity.class, badgeId);
            userInfoEntity.deleteBadge(badgeEntity);
            session.update(userInfoEntity);
            session.getTransaction().commit();
            log.info("Badge id: {} deleted from user id {} in DB at: {}", badgeId, authId, LocalDateTime.now());
            return UserInfoConvert.toDto(userInfoEntity);
        } catch (PersistenceException e) {
            log.info("Fail to delete badge id: {} from user id {} in DB at: {}", badgeId, authId, LocalDateTime.now());
            throw new RuntimeException(e);
        }
    }
}
