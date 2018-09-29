package com.linkedin.backend.handlers.exception;

public class ConnectionNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "Connection doesn't exist";
    }
}
