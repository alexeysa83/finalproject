package com.github.alexeysa83.finalproject.service.user;

import com.github.alexeysa83.finalproject.model.dto.UserDto;

public interface UserService {

//   User create/delete service is made in transaction with AuthUserDAO methods

    UserDto getById (long id);

    boolean update (UserDto user);
}
