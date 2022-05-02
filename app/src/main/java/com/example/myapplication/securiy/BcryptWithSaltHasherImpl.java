package com.example.myapplication.securiy;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BcryptWithSaltHasherImpl implements PasswordHasher {
    private static final Logger LOG = LoggerFactory.getLogger(BcryptWithSaltHasherImpl.class);
    private static final String SALT = BCrypt.gensalt();

    @Override
    public boolean checkIsEqualsPasswordAndPasswordHash(String password, String passwordHash) {
        LOG.info("Passwords was equals ##checkIsEqualsPasswordAndPasswordHash method##");
        return BCrypt.checkpw(password, passwordHash);
    }

    @Override
    public String hashPassword(String password) {
        LOG.info("Password was hashed");
        return BCrypt.hashpw(password, SALT);
    }

}
