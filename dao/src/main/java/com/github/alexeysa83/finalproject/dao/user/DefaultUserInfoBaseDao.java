package com.github.alexeysa83.finalproject.dao.user;

import com.github.alexeysa83.finalproject.dao.ConvertEntityDTO;
import com.github.alexeysa83.finalproject.dao.HibernateUtil;
import com.github.alexeysa83.finalproject.dao.entity.UserInfoEntity;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;

public class DefaultUserInfoBaseDao implements UserInfoBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserInfoBaseDao.class);

    private static volatile UserInfoBaseDao instance;

    public static UserInfoBaseDao getInstance() {
        UserInfoBaseDao localInstance = instance;
        if (localInstance == null) {
            synchronized (UserInfoBaseDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultUserInfoBaseDao();
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
    public UserInfoDto getById(long authId) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        entityManager.getTransaction().begin();
        final UserInfoEntity userInfoEntity = entityManager.find(UserInfoEntity.class, authId);
        entityManager.getTransaction().commit();
        entityManager.close();
        return ConvertEntityDTO.UserToDto(userInfoEntity);
    }

    @Override
    public boolean update(UserInfoDto userInfoDto) {
        try {
            UserInfoEntity userToUpdate = ConvertEntityDTO.UserToEntity(userInfoDto);
            EntityManager entityManager = HibernateUtil.getEntityManager();
            entityManager.getTransaction().begin();

            userToUpdate.setFirstName(userInfoDto.getFirstName());
            userToUpdate.setLastName(userInfoDto.getLastName());
            userToUpdate.setEmail(userInfoDto.getEmail());
            userToUpdate.setPhone(userInfoDto.getPhone());
//            userToUpdate.getAuthUser().setLogin("HAHA");

            entityManager.merge(userToUpdate);
            entityManager.getTransaction().commit();
            entityManager.close();
            log.info("User id: {} updated in DB at: {}", userInfoDto.getAuthId(), LocalDateTime.now());
            return true;
        } catch (PersistenceException | NullPointerException e) {
            log.error("Fail to update in DB User: {} at: {}", userInfoDto, LocalDateTime.now(), e);
            return false;
        }

    }
}
