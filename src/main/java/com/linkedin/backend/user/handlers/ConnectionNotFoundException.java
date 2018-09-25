package com.linkedin.backend.user.handlers;

public class ConnectionNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "Connection doesn't exist";
    }
}
