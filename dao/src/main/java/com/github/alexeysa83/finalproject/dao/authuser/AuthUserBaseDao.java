package com.github.alexeysa83.finalproject.dao.authuser;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;

public interface AuthUserBaseDao extends BaseDao<AuthUserDto> {

    AuthUserDto add(AuthUserDto user);

    AuthUserDto getByLogin(String login);

    boolean delete (long id);
}
