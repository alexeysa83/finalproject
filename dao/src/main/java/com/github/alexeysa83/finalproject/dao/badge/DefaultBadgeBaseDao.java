package com.github.alexeysa83.finalproject.dao.badge;

import com.github.alexeysa83.finalproject.dao.ConvertEntityDTO;
import com.github.alexeysa83.finalproject.dao.HibernateUtil;
import com.github.alexeysa83.finalproject.dao.entity.BadgeEntity;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DefaultBadgeBaseDao implements BadgeBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultBadgeBaseDao.class);

    private static volatile BadgeBaseDao instance;

    public static BadgeBaseDao getInstance() {
        BadgeBaseDao localInstance = instance;
        if (localInstance == null) {
            synchronized (BadgeBaseDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultBadgeBaseDao();
                }
            }
        }
        return localInstance;
    }

    @Override
    public BadgeDto add(BadgeDto badgeDto) {

        final BadgeEntity badgeEntity = ConvertEntityDTO.BadgeToEntity(badgeDto);
//        try (Session session = HibernateUtil.getSession()) {
//
//        }
        Session session = HibernateUtil.getSession();
            session.beginTransaction();
            session.save(badgeEntity);
            session.getTransaction().commit();
//        }
//        catch (PersistenceException e) {
//            log.error("Fail to save new badge to DB: {}, at: {}", badgeDto, LocalDateTime.now(), e);
//            return null;
//        }
                log.info("Badge id: {} saved to DB at: {}", badgeEntity.getId(), LocalDateTime.now());
        return ConvertEntityDTO.BadgeToDto(badgeEntity);
    }

    @Override
    public boolean isNameTaken(String name) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from BadgeEntity b where b.badgeName=:name");
            BadgeEntity badgeEntity = (BadgeEntity) query.setParameter("name", name).getSingleResult();
            session.getTransaction().commit();
            return badgeEntity != null;
        } catch (PersistenceException e) {
            log.error("Fail to get badge from DB by name: {}, at: {}", name, LocalDateTime.now(), e);
            return false;
        }
    }

    @Override
    public BadgeDto getById(long id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        final BadgeEntity badgeEntity = session.get(BadgeEntity.class, id);
        session.getTransaction().commit();
        session.close();
        return ConvertEntityDTO.BadgeToDto(badgeEntity);
    }

    @Override
    public List<BadgeDto> getAll() {
        List<BadgeDto> badgeDtos = new ArrayList<>();
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();

            Query query = session.createQuery("from BadgeEntity b order by b.id")
                    .setReadOnly(true);
            List<BadgeEntity> list = query.list();
            session.getTransaction().commit();
            list.forEach(badgeEntity -> {
                badgeDtos.add(ConvertEntityDTO.BadgeToDto(badgeEntity));
            });
            return badgeDtos;
        } catch (PersistenceException e) {
            log.error("Fail to get list of badges from DB at: {}", LocalDateTime.now(), e);
            return null;
        }
    }

    @Override
    public boolean update(BadgeDto badgeDto) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();

            final int i = session.createQuery("update BadgeEntity b set b.badgeName=:badgeName where b.id=:id")
                    .setParameter("badgeName", badgeDto.getBadgeName())
                    .setParameter("id", badgeDto.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            log.info("Badge id: {} updated in DB at: {}", badgeDto.getId(), LocalDateTime.now());
            return i > 0;
        } catch (PersistenceException e) {
            log.error("Fail to update in DB badge: {} at: {}", badgeDto, LocalDateTime.now(), e);
            return false;
        }
    }

    @Override
    public boolean delete(long id) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            final int i = session.createQuery("delete BadgeEntity b where b.id=:id")
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
            log.info("Badge id: {} deleted from DB at: {}", id, LocalDateTime.now());
            return i > 0;
        } catch (PersistenceException e) {
            log.error("Fail to delete badge id from DB: {}, at: {}", id, LocalDateTime.now(), e);
            return false;
        }
    }
}
