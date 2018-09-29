package com.linkedin.backend.handlers;

public class DuplicateConnectionException extends Exception {

    @Override
    public String getMessage() {
        return "Connection already exists";
    }
}
