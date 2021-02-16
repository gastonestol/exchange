package com.bitso.challenge.db;

import com.bitso.challenge.db.entity.User;

import java.util.Optional;

/**
 * Model to handler users.
 */
public interface UserModel {

    Optional<User> get(long id);
}
