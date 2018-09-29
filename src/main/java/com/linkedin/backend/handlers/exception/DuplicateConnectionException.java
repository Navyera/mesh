package com.linkedin.backend.handlers.exception;

public class DuplicateConnectionException extends Exception {

    @Override
    public String getMessage() {
        return "Connection already exists";
    }
}
