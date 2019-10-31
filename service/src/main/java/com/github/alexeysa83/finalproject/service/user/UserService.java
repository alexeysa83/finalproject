package com.github.alexeysa83.finalproject.service.user;

import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;

public interface UserService {

//   User create/delete service is made in transaction with AuthUserDAO methods

    UserInfoDto getById (long id);

    boolean update (UserInfoDto user);
}
