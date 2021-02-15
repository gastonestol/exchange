package com.bitso.challenge.model;

import com.bitso.challenge.entity.User;

import java.util.Optional;

/**
 * Model to handler users.
 */
public interface UserModel {

    Optional<User> get(long id);
}
