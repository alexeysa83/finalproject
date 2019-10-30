package com.github.alexeysa83.finalproject.dao.user;

import com.github.alexeysa83.finalproject.dao.ConvertEntityDTO;
import com.github.alexeysa83.finalproject.dao.HibernateUtil;
import com.github.alexeysa83.finalproject.dao.entity.UserEntity;
import com.github.alexeysa83.finalproject.model.dto.UserDto;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.time.LocalDateTime;

public class DefaultUserBaseDao implements UserBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserBaseDao.class);

    private static volatile UserBaseDao instance;

    public static UserBaseDao getInstance() {
        UserBaseDao localInstance = instance;
        if (localInstance == null) {
            synchronized (UserBaseDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultUserBaseDao();
                }
            }
        }
        return localInstance;
    }

    // Create and delete user logic in AuthUserDAO methods in transactions
    /*
    Get by auth_id method
     */

    // Get by auth_id method
    @Override
    public UserDto getById(long authId) {
        try {
            EntityManager entityManager = HibernateUtil.getEntityManager();
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("select au.user from AuthUserEntity as au where au.id = :authId");
            UserEntity userEntity = (UserEntity) query.setParameter("authId", authId).getSingleResult();
            entityManager.getTransaction().commit();
            entityManager.close();
            return ConvertEntityDTO.UserToDto(userEntity);
        } catch (PersistenceException e) {
            log.error("Fail to get by id user {} at: {}", authId, LocalDateTime.now(), e);
           return null;
        }
    }

    @Override
    public boolean update(UserDto userDto) {
        UserEntity userToUpdate = getById(userDto.getAuthId());
        try {
            EntityManager entityManager = HibernateUtil.getEntityManager();
            entityManager.getTransaction().begin();

            userToUpdate.setFirstName(userDto.getFirstName());
            userToUpdate.setLastName(userDto.getLastName());
            userToUpdate.setEmail(userDto.getEmail());
            userToUpdate.setPhone(userDto.getPhone());
//            userToUpdate.getAuthUser().setLogin("HAHA");

            entityManager.merge(userToUpdate);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.info("User id: {} updated in DB at: {}", userDto.getId(), LocalDateTime.now());
            return true;
        } catch (HibernateException | NullPointerException e) {
            log.error("Fail to update in DB User: {} at: {}", userDto, LocalDateTime.now(), e);
            return false;
        }
    }

    //    @Override
//    public boolean update(UserDto userDto) {
//        try (Connection connection = mysql.getConnection();
//             PreparedStatement statement = connection.prepareStatement
//                     ("update user set first_name = ?, last_name = ?, email = ?, phone = ? where auth_id = ?")) {
//            statement.setString(1, userDto.getFirstName());
//            statement.setString(2, userDto.getLastName());
//            statement.setString(3, userDto.getEmail());
//            statement.setString(4, userDto.getPhone());
//            statement.setLong(5, userDto.getAuthId());
//            log.info("User (auth_id): {} updated in DB at: {}", userDto.getAuthId(), LocalDateTime.now());
//            return statement.executeUpdate() > 0;
//        } catch (SQLException e) {
//            log.error("SQLException at: {}", LocalDateTime.now(), e);
//            throw new RuntimeException(e);
//        }
//    }
}
