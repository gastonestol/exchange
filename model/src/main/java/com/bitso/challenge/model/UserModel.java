package com.bitso.challenge.model;

import com.bitso.challenge.model.entity.User;

import java.util.Optional;

/**
 * Model to handler users.
 */
public interface UserModel {

    Optional<User> get(long id);
    Optional<User> get(String email);

}
