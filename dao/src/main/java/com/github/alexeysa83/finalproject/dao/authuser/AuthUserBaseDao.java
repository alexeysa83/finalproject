package com.github.alexeysa83.finalproject.dao.authuser;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.AuthUser;

import java.sql.Timestamp;

public interface AuthUserBaseDao extends BaseDao<AuthUser> {

    AuthUser createAndSave(AuthUser user, Timestamp regTime);

    AuthUser getByLogin(String login);

    boolean delete (long id);

}
