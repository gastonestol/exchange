package com.bitso.challenge.model.ram;

import com.bitso.challenge.entity.User;
import com.bitso.challenge.model.UserModel;

import java.util.HashMap;
import java.util.Optional;

/**
 * RAM-based implementation of UserModel.
 */
public class UserModelImpl implements UserModel {

    private HashMap<Long,User> users = new HashMap<>(10);

    @Override
    public Optional<User> get(long id) {
        return Optional.ofNullable(users.get(id));
    }

    public void add(User user) {
        users.put(user.getId(), user);
    }
}
