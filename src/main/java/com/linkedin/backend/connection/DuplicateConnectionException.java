package com.linkedin.backend.connection;

public class DuplicateConnectionException extends Exception {

    @Override
    public String getMessage() {
        return "Connection already exists";
    }
}
