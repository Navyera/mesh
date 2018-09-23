package com.linkedin.backend.connection;

public class ConnectionNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "Connection doesn't exist";
    }
}
