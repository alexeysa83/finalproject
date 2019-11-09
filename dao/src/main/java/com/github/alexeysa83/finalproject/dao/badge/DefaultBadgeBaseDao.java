package com.github.alexeysa83.finalproject.dao.badge;

import com.github.alexeysa83.finalproject.dao.HibernateUtil;
import com.github.alexeysa83.finalproject.dao.convert.BadgeConvert;
import com.github.alexeysa83.finalproject.dao.entity.BadgeEntity;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        final BadgeEntity badgeEntity = BadgeConvert.toEntity(badgeDto);
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.save(badgeEntity);
            session.getTransaction().commit();
            log.info("Badge id: {} saved to DB at: {}", badgeEntity.getId(), LocalDateTime.now());
            return BadgeConvert.toDto(badgeEntity);
        } catch (PersistenceException e) {
            log.error("Fail to save new badge to DB: {}, at: {}", badgeDto, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isNameTaken(String name) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from BadgeEntity b where b.badgeName=:name");
            BadgeEntity badgeEntity = (BadgeEntity) query.setParameter("name", name).uniqueResult();
            session.getTransaction().commit();
            return badgeEntity != null;
        } catch (PersistenceException e) {
            log.error("Fail to get badge from DB by name: {}, at: {}", name, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Used only in tests
     */
    @Override
    public BadgeDto getById(long id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        final BadgeEntity badgeEntity = session.get(BadgeEntity.class, id);
        session.getTransaction().commit();
        session.close();
        return BadgeConvert.toDto(badgeEntity);
    }

    /**
     * Testing?
     */
    @Override
    public List<BadgeDto> getAll() {
        List<BadgeDto> badgeDtos;
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();

            final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            final CriteriaQuery<BadgeEntity> criteria = criteriaBuilder.createQuery(BadgeEntity.class);
            final Root<BadgeEntity> badge = criteria.from(BadgeEntity.class);
            criteria.select(badge).orderBy(criteriaBuilder.asc(badge.get("badgeName")));
            final List<BadgeEntity> listEntities = session.createQuery(criteria).getResultList();

            session.getTransaction().commit();
            badgeDtos = listEntities.stream().map(BadgeConvert::toDto).collect(Collectors.toList());
            return badgeDtos;
        } catch (PersistenceException e) {
            log.error("Fail to get list of badges from DB at: {}", LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    //    @Override
//    public List<BadgeDto> getAll() {
//        List<BadgeDto> badgeDtos = new ArrayList<>();
//        try (Session session = HibernateUtil.getSession()) {
//            session.beginTransaction();
//
//            Query query = session.createQuery("from BadgeEntity b order by b.id")
//                    .setReadOnly(true);
//            List<BadgeEntity> list = query.list();
//            session.getTransaction().commit();
//            list.forEach(badgeEntity -> {
//                badgeDtos.add(ConvertEntityDTO.BadgeToDto(badgeEntity));
//            });
//            return badgeDtos;
//        } catch (PersistenceException e) {
//            log.error("Fail to get list of badges from DB at: {}", LocalDateTime.now(), e);
//            return null;
//        }
//    }

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
            throw new RuntimeException(e);
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
