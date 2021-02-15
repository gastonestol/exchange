package com.bitso.challenge.entity;

import lombok.Data;

/**
 * Represents a user in the system.
 */
@Data
public class User {
    private long id;
    private String email;
    private String password;

    public void setPassword(String password) {
        //TODO encrypt
        this.password = password;
    }
}
