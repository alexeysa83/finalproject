package com.github.alexeysa83.finalproject.dao.authuser;

import com.github.alexeysa83.finalproject.dao.convert_entity.AuthUserConvert;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.repository.AuthUserRepository;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public class DefaultAuthUserBaseDao implements AuthUserBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultAuthUserBaseDao.class);

    private final AuthUserRepository authRepository;

    public DefaultAuthUserBaseDao(AuthUserRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public AuthUserDto add(AuthUserDto authUserDto) {
        final AuthUserEntity authUserEntity = AuthUserConvert.toEntity(authUserDto);
        final AuthUserEntity savedToDB = authRepository.save(authUserEntity);
        if (savedToDB != null) {
            log.info("AuthUser id: {} saved to DB at: {}", savedToDB.getId(), LocalDateTime.now());
        } else {
            log.error("Fail to save new user to DB: {}, at: {}", authUserDto, LocalDateTime.now());
        }
        return AuthUserConvert.toDto(savedToDB);
    }

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
        final int rowsUpdated = authRepository.updateLoginPasswordRole(
                authUserDto.getId(),
                authUserDto.getLogin(),
                authUserDto.getPassword(),
                authUserDto.getRole());
        if (rowsUpdated > 0) {
            log.info("AuthUser id: {} updated in DB at: {}", authUserDto.getId(), LocalDateTime.now());
        } else {
            log.error("Fail to update in DB AuthUser: {} at: {}", authUserDto, LocalDateTime.now());
        }
        return rowsUpdated > 0;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean delete(Long id) {
        final int rowsUpdated = authRepository.isDeletedSetTrue(id);
        if (rowsUpdated > 0) {
            log.info("AuthUser id : {} deleted from DB at: {}", id, LocalDateTime.now());
        } else {
            log.error("Fail to delete AuthUser id: {} from DB, at: {}", id, LocalDateTime.now());
        }
        return rowsUpdated > 0;
    }
}