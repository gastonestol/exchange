package com.bitso.challenge.model;

import com.bitso.challenge.model.entity.User;

import java.util.Optional;

/**
 * Model to handler users.
 */
public interface UserModel {

    Optional<User> get(Long id);
    Optional<User> get(String email);

}
