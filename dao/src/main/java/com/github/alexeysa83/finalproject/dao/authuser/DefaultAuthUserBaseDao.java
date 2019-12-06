package com.github.alexeysa83.finalproject.dao.authuser;

import com.github.alexeysa83.finalproject.dao.convert_entity.AuthUserConvert;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.repository.AuthUserRepository;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.Optional;

public class DefaultAuthUserBaseDao implements AuthUserBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultAuthUserBaseDao.class);

    private final AuthUserRepository authRepository;

    public DefaultAuthUserBaseDao(AuthUserRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     *
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public AuthUserDto add(AuthUserDto authUserDto) {
        final AuthUserEntity authUserEntity = AuthUserConvert.toEntity(authUserDto);
        AuthUserEntity savedToDB;
        try {
            savedToDB = authRepository.save(authUserEntity);
//        } catch (PersistenceException | IllegalArgumentException e) {
        } catch (RuntimeException e) {
            log.error("Fail to save new user to DB: {}, at: {}", authUserDto, LocalDateTime.now(), e);
//            throw new RuntimeException(e);
            return null;
        }
        log.info("AuthUser id: {} saved to DB at: {}", savedToDB.getId(), LocalDateTime.now());
        return AuthUserConvert.toDto(savedToDB);
    }

    /**
     * Need try/catch?
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AuthUserDto getByLogin(String login) {
        final AuthUserEntity authUserEntity = authRepository.findByLogin(login);
        return AuthUserConvert.toDto(authUserEntity);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AuthUserDto getById(Long id) {
        final Optional<AuthUserEntity> optional = authRepository.findById(id);
        if (optional.isPresent()) {
            final AuthUserEntity authUserEntity = optional.get();
            return AuthUserConvert.toDto(authUserEntity);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean update(AuthUserDto authUserDto) {
        String message = "AuthUser id: {} updated in DB at: {}";
        try {
            final int rowsUpdated = authRepository.updateLoginPasswordRole(
                    authUserDto.getId(),
                    authUserDto.getLogin(),
                    authUserDto.getPassword(),
                    authUserDto.getRole());
            if (rowsUpdated <= 0) {
                message = "Fail to update in DB AuthUser: {} at: {}";
            }
            log.info(message, authUserDto.getId(), LocalDateTime.now());
            return rowsUpdated > 0;
        } catch (PersistenceException e) {
            log.error("Fail to update in DB AuthUser: {} at: {}", authUserDto, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean delete(Long id) {
        String message = "AuthUser id : {} deleted from DB at: {}";
        try {
            final int rowsUpdated = authRepository.isDeletedSetTrue(id);
            if (rowsUpdated <= 0) {
                message = "Fail to delete AuthUser id: {} from DB, at: {}";
            }
            log.info(message, id, LocalDateTime.now());
            return rowsUpdated > 0;
        } catch (PersistenceException e) {
            log.error("Fail to delete AuthUser id: {} from DB, at: {}", id, LocalDateTime.now());
            throw new RuntimeException(e);
        }
    }
}