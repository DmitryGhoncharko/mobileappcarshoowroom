package com.example.myapplication.exception;

public class ServiceError extends Error {
    public ServiceError() {
    }

    public ServiceError(String message) {
        super(message);
    }

    public ServiceError(String message, Throwable cause) {
        super(message, cause);
    }
}
