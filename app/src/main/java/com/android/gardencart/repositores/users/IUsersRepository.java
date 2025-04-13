package com.android.gardencart.repositores.users;

import com.android.gardencart.models.User;

import java.util.List;

public interface IUsersRepository {
    List<User> getUsers();
    User getUserByUsername(String username);
    boolean checkUserPassword(String username, String password);
}
