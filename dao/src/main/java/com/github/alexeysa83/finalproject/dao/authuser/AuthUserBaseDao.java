package com.github.alexeysa83.finalproject.dao.authuser;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;

import java.sql.Timestamp;

public interface AuthUserBaseDao extends BaseDao<AuthUserDto> {

    AuthUserDto createAndSave(AuthUserDto user, Timestamp regTime);

    AuthUserDto getByLogin(String login);

    boolean delete (long id);

}
