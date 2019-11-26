package com.github.alexeysa83.finalproject.dao.authuser;

import com.github.alexeysa83.finalproject.dao.SessionManager;
import com.github.alexeysa83.finalproject.dao.convert_entity.AuthUserConvert;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;

public class DefaultAuthUserBaseDao extends SessionManager<AuthUserDto> implements AuthUserBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultAuthUserBaseDao.class);

    public DefaultAuthUserBaseDao(EntityManagerFactory factory) {
        super(factory);
    }

    // default role and block status in DB or constructor???
    @Override
    public AuthUserDto add(AuthUserDto user) {
        final AuthUserEntity authUserEntity = AuthUserConvert.toEntity(user);
        try (Session session = getSession()) {
            session.beginTransaction();
            session.save(authUserEntity);
            session.getTransaction().commit();
            log.info("AuthUser id: {} saved to DB at: {}", authUserEntity.getId(), LocalDateTime.now());
            return AuthUserConvert.toDto(authUserEntity);
        } catch (PersistenceException e) {
            log.error("Fail to save new user to DB: {}, at: {}", user, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUserDto getByLogin(String login) {
        try (Session session = getSession()) {
            session.beginTransaction();
            Query query = session.createQuery("from AuthUserEntity where login = :login").setCacheable(true);
            AuthUserEntity authUserEntity = (AuthUserEntity) query.setParameter("login", login).uniqueResult();
            session.getTransaction().commit();
            return AuthUserConvert.toDto(authUserEntity);
        } catch (PersistenceException e) {
            log.error("Fail to get user from DB by login: {}, at: {}", login, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUserDto getById(long id) {
        Session session = getSession();
        session.beginTransaction();
        final AuthUserEntity authUser = session.get(AuthUserEntity.class, id);
        session.getTransaction().commit();
        final AuthUserDto authUserDto = AuthUserConvert.toDto(authUser);
        session.close();
        return authUserDto;
    }

    @Override
    public boolean update(AuthUserDto authUserDto) {

        try (Session session = getSession()) {
            session.beginTransaction();

            final int i = session.createQuery("update AuthUserEntity a set a.login=:login," +
                    " a.password=:password, a.role=:role where a.id=:id")
                    .setParameter("login", authUserDto.getLogin())
                    .setParameter("password", authUserDto.getPassword())
                    .setParameter("role", authUserDto.getRole())
                    .setParameter("id", authUserDto.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            log.info("AuthUser id: {} updated in DB at: {}", authUserDto.getId(), LocalDateTime.now());
            return i > 0;
        } catch (PersistenceException e) {
            log.error("Fail to update in DB AuthUser: {} at: {}", authUserDto, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * AuthUser flag isDeleted set to true, UserInfo deleted
     */
    @Override
    public boolean delete(long id) {
        try (Session session = getSession()) {
            session.beginTransaction();
            final int userInfoDeleted = session.createQuery("delete UserInfoEntity u where u.id=:id")
                    .setParameter("id", id)
                    .executeUpdate();
            session.flush();
            int authUserDeleted = 0;
            if (userInfoDeleted > 0) {
                authUserDeleted = session.createQuery
                        ("update AuthUserEntity a set a.deleted=:isDeleted where a.id=:id")
                        .setParameter("isDeleted", true)
                        .setParameter("id", id)
                        .executeUpdate();
            }
            session.getTransaction().commit();
            log.info("AuthUser id : {} deleted from DB at: {}", id, LocalDateTime.now());
            return authUserDeleted > 0;
        } catch (PersistenceException e) {
            log.error("Fail to delete AuthUser id: {} from DB, at: {}", id, LocalDateTime.now());
            throw new RuntimeException(e);
        }
    }
}