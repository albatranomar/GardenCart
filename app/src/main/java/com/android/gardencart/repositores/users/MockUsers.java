package com.android.gardencart.repositores.users;

import com.android.gardencart.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockUsers implements IUsersRepository {
    private static MockUsers instance;

    private List<User> users;

    public MockUsers() {
        users = new ArrayList<>();
        users.add(new User("omar", "omar"));
        users.add(new User("ali", "ali"));
        users.add(new User("ahmad", "ahmad"));
    }

    public static MockUsers getInstance() {
        if (instance == null) {
            instance = new MockUsers();
        }

        return instance;
    }

    @Override
    public List<User> getUsers() {
        if (users.isEmpty()) {
            users.add(new User("omar", "omar"));
            users.add(new User("ali", "ali"));
            users.add(new User("ahmad", "ahmad"));
        }

        return users;
    }

    @Override
    public User getUserByUsername(String username) {
        Optional<User> user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
        return user.orElse(null);
    }

    @Override
    public boolean checkUserPassword(String username, String password) {
        User u = getUserByUsername(username);

        return u.getPassword().equals(password);
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
