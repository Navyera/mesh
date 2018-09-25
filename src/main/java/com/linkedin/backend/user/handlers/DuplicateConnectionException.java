package com.linkedin.backend.user.handlers;

public class DuplicateConnectionException extends Exception {

    @Override
    public String getMessage() {
        return "Connection already exists";
    }
}
