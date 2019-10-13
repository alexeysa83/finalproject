package com.github.alexeysa83.finalproject.service.user;

import com.github.alexeysa83.finalproject.model.User;

public interface UserService {

//   User create/delete service is made in transaction with AuthUserDAO methods

    User getById (String id);

    boolean update (User user);
}
