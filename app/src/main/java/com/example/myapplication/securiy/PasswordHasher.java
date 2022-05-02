package com.example.myapplication.securiy;

public interface PasswordHasher {

    boolean checkIsEqualsPasswordAndPasswordHash(String password, String passwordHash);

    String hashPassword(String password);
}
