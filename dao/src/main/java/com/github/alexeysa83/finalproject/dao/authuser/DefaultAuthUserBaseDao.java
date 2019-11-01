package com.github.alexeysa83.finalproject.dao.authuser;

import com.github.alexeysa83.finalproject.dao.ConvertEntityDTO;
import com.github.alexeysa83.finalproject.dao.HibernateUtil;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;

public class DefaultAuthUserBaseDao implements AuthUserBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultAuthUserBaseDao.class);

    private static volatile AuthUserBaseDao instance;

    public static AuthUserBaseDao getInstance() {
        AuthUserBaseDao localInstance = instance;
        if (localInstance == null) {
            synchronized (AuthUserBaseDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultAuthUserBaseDao();
                }
            }
        }
        return localInstance;
    }

    // default role and block status in DB or constructor???

    @Override
    public AuthUserDto createAndSave(AuthUserDto user) {
        final AuthUserEntity authUserEntity = ConvertEntityDTO.AuthUserToEntity(user);
        try {
            Session session = HibernateUtil.getSession();
            session.beginTransaction();
            session.save(authUserEntity);
            session.getTransaction().commit();
            session.close();
        } catch (PersistenceException e) {
            log.error("Fail to save new user to DB: {}, at: {}", user, LocalDateTime.now(), e);
            return null;
        }
        log.info("AuthUser id: {} saved to DB at: {}", authUserEntity.getId(), LocalDateTime.now());
        return ConvertEntityDTO.AuthUserToDto(authUserEntity);
    }

//    @Override
//    public AuthUserDto createAndSave(AuthUserDto user, Timestamp regTime) {
//        Connection connection = null;
//        final String login = user.getLogin();
//        final String password = user.getPassword();
//        try {
//            connection = mysql.getConnection();
//            connection.setAutoCommit(false);
//            try (PreparedStatement stateAuthUser = connection.prepareStatement
//                    ("insert into auth_user (login, password) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
//                 PreparedStatement stateUser = connection.prepareStatement
//                         ("insert into user (registration_time, auth_id) values (?, ?)")) {
//                stateAuthUser.setString(1, login);
//                stateAuthUser.setString(2, password);
//                stateAuthUser.executeUpdate();
//                long id;
//                try (ResultSet generatedKeys = stateAuthUser.getGeneratedKeys()) {
//                    final boolean isSaved = generatedKeys.next();
//                    if (!isSaved) {
//                        return null;
//                    }
//                    id = generatedKeys.getLong(1);
//                }
//                stateUser.setTimestamp(1, regTime);
//                stateUser.setLong(2, id);
//                stateUser.executeUpdate();
//                connection.commit();
//                log.info("AuthUser id (User auth_id): {} saved to DB at: {}", id, LocalDateTime.now());
//                return new AuthUserDto(id, login, password, Role.USER, false);
//            }
//        } catch (SQLException e) {
//            try {
//                connection.rollback();
//                log.error("Unable to save new user to DB with login: {}, password: {}, at: {}",
//                        login, password, LocalDateTime.now());
//            } catch (SQLException ex) {
//                log.error("Unable to rollback transaction at: {}", LocalDateTime.now(), ex);
//                throw new RuntimeException(ex);
//            }
//            log.error("SQLException at: {}", LocalDateTime.now(), e);
//            throw new RuntimeException(e);
//        } finally {
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    log.error("Unable to close connection at: {}", LocalDateTime.now(), e);
//                }
//            }
//        }
//    }

    @Override
    public AuthUserDto getByLogin(String login) {
        try (Session session = HibernateUtil.getSession()){
//            Session session = HibernateUtil.getSession();
            session.beginTransaction();

            Query query = session.createQuery("from AuthUserEntity where login = :login");
            AuthUserEntity authUserEntity = (AuthUserEntity) query.setParameter("login", login).getSingleResult();
            session.getTransaction().commit();
            final AuthUserDto authUserDto = ConvertEntityDTO.AuthUserToDto(authUserEntity);
//            session.close();
            return authUserDto;
        } catch (PersistenceException e) {
            log.error("Fail to get user from DB by login: {}, at: {}", login, LocalDateTime.now(), e);
            return null;
        }
    }

//    @Override
//    public AuthUserDto getByLogin(String login) {
//        try (Connection connection = mysql.getConnection();
//             PreparedStatement statement = connection.prepareStatement
//                     ("select * from auth_user where login = ?")) {
//            statement.setString(1, login);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                final boolean exist = resultSet.next();
//                if (!exist) {
//                    return null;
//                }
//                final long id = resultSet.getLong("id");
//                final String password = resultSet.getString("password");
//                final String r = resultSet.getString("role");
//                final Role role = Role.valueOf(r);
//                boolean isBlocked = resultSet.getBoolean("is_blocked");
//                return new AuthUserDto(id, login, password, role, isBlocked);
//            }
//        } catch (SQLException e) {
//            log.error("SQLException at: {}", LocalDateTime.now(), e);
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public AuthUserDto getById(long id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        final AuthUserEntity authUser = session.get(AuthUserEntity.class, id);
        session.getTransaction().commit();
        final AuthUserDto authUserDto = ConvertEntityDTO.AuthUserToDto(authUser);
        session.close();
        return authUserDto;
    }

//    @Override
//    public AuthUserDto getById(long id) {
//        try (Connection connection = mysql.getConnection();
//             PreparedStatement statement = connection.prepareStatement
//                     ("select * from auth_user where id = ?")) {
//            statement.setLong(1, id);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                final boolean exist = resultSet.next();
//                if (!exist) {
//                    return null;
//                }
//                final String login = resultSet.getString("login");
//                final String password = resultSet.getString("password");
//                final String r = resultSet.getString("role");
//                final Role role = Role.valueOf(r);
//                final boolean isBlocked = resultSet.getBoolean("is_blocked");
//                return new AuthUserDto(id, login, password, role, isBlocked);
//            }
//        } catch (SQLException e) {
//            log.error("SQLException at: {}", LocalDateTime.now(), e);
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public boolean update(AuthUserDto authUserDto) {

        try {
            Session session = HibernateUtil.getSession();
            session.beginTransaction();

            final int i = session.createQuery("update AuthUserEntity a set a.login=:login," +
                    " a.password=:password, a.role=:role, a.deleted=:isDeleted where a.id=:id")
                    .setParameter("login", authUserDto.getLogin())
                    .setParameter("password", authUserDto.getPassword())
                    .setParameter("role", authUserDto.getRole())
                    .setParameter("isDeleted", authUserDto.isDeleted())
                    .setParameter("id", authUserDto.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
            log.info("AuthUser id: {} updated in DB at: {}", authUserDto.getId(), LocalDateTime.now());
            return i > 0;
        } catch (PersistenceException e) {
            log.error("Fail to update in DB AuthUser: {} at: {}", authUserDto, LocalDateTime.now(), e);
            return false;
        }
    }

//    @Override
//    public boolean update(AuthUserDto user) {
//        final AuthUserEntity authUserToUpdate = ConvertEntityDTO.AuthUserToEntity(user);
//        try {
//            Session session = HibernateUtil.getSession();
//            session.beginTransaction();
//            session.update(authUserToUpdate);
//            session.getTransaction().commit();
//            session.close();
//            log.info("AuthUser id: {} updated in DB at: {}", user.getId(), LocalDateTime.now());
//            return true;
//        } catch (PersistenceException e) {
//            log.error("Fail to update in DB AuthUser id: {} at: {}", user.getId(), LocalDateTime.now(), e);
//            return false;
//        }
//
//    }


//    @Override
//    public boolean update(AuthUserDto user) {
//        try (Connection connection = mysql.getConnection();
//             PreparedStatement statement = connection.prepareStatement
//                     ("update auth_user set login = ?, password = ?, role = ? where id = ?")) {
//            statement.setString(1, user.getLogin());
//            statement.setString(2, user.getPassword());
//            statement.setString(3, user.getRole().toString());
//            statement.setLong(4, user.getId());
//            log.info("AuthUser id: {} updated in DB at: {}", user.getId(), LocalDateTime.now());
//            return statement.executeUpdate() > 0;
//        } catch (SQLException e) {
//            log.error("SQLException at: {}", LocalDateTime.now(), e);
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public boolean delete(long id) {
        try {
            Session session = HibernateUtil.getSession();
            session.beginTransaction();
            AuthUserEntity authUserToDelete = session.get(AuthUserEntity.class, id);
            authUserToDelete.getUser().setBadges(null);
            session.flush();
            authUserToDelete.setUser(null);
            authUserToDelete.setDeleted(true);
            session.update(authUserToDelete);
            session.getTransaction().commit();
            session.close();
            log.info("AuthUser id : {} deleted from DB at: {}", id, LocalDateTime.now());
            return true;
        } catch (PersistenceException e) {
            log.error("Fail to delete AuthUser id: {} from DB, at: {}", id, LocalDateTime.now());
            return false;
        }
    }

//    @Override
//    public boolean delete(long id) {
//        Connection connection = null;
//        try {
//            connection = mysql.getConnection();
//            connection.setAutoCommit(false);
//            try (PreparedStatement stateAuthUser = connection.prepareStatement
//                    ("update auth_user set is_blocked = true where id = ?");
//                 PreparedStatement stateUser = connection.prepareStatement
//                         ("delete from user where auth_id = ?")) {
//                stateAuthUser.setLong(1, id);
//                final int i = stateAuthUser.executeUpdate();
//                stateUser.setLong(1, id);
//                int b = stateUser.executeUpdate();
//                connection.commit();
//                log.info("AuthUser id (User auth_id): {} deleted from DB at: {}", id, LocalDateTime.now());
//                return i > 0 && b > 0;
//            }
//        } catch (SQLException e) {
//            try {
//                connection.rollback();
//                log.error("Unable to delete AuthUser id: {} from DB, at: {}", id, LocalDateTime.now());
//            } catch (SQLException ex) {
//                log.error("Unable to rollback transaction at: {}", LocalDateTime.now(), ex);
//                throw new RuntimeException(ex);
//            }
//            log.error("SQLException at: {}", LocalDateTime.now(), e);
//            throw new RuntimeException(e);
//        } finally {
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    log.error("Unable to close connection at: {}", LocalDateTime.now(), e);
//                }
//            }
//        }
//    }
}