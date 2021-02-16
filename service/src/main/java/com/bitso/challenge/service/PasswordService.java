package com.bitso.challenge.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public static final String BCRYPT_SALT = BCrypt.gensalt(10);
    public String hashPassword(String password){
        return BCrypt.hashpw(password, BCRYPT_SALT);
    }
    public boolean checkPassword(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }

}
