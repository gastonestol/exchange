package com.bitso.challenge.model.entity;

import lombok.Data;

/**
 * Represents a user in the system.
 */
@Data
public class User {
    private Long id;
    private String email;
    private String password;

    public void setPassword(String password) {
        //TODO encrypt
        this.password = password;
    }
}
