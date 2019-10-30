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

    @Override
    public UserDto getById(long authId) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        entityManager.getTransaction().begin();
        final UserEntity userEntity = entityManager.find(UserEntity.class, authId);
        entityManager.getTransaction().commit();
        entityManager.close();
        return ConvertEntityDTO.UserToDto(userEntity);
    }

    @Override
    public boolean update(UserDto userDto) {
        try {
            UserEntity userToUpdate = ConvertEntityDTO.UserToEntity(userDto);
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
            log.info("User id: {} updated in DB at: {}", userDto.getAuthId(), LocalDateTime.now());
            return true;
        } catch (PersistenceException | NullPointerException e) {
            log.error("Fail to update in DB User: {} at: {}", userDto, LocalDateTime.now(), e);
            return false;
        }

    }
}
