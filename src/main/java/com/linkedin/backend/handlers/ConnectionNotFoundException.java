package com.linkedin.backend.handlers;

public class ConnectionNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "Connection doesn't exist";
    }
}
