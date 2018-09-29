package com.linkedin.backend.handlers.exception;

public class DuplicateUserException extends Exception {
    @Override
    public String getMessage() {
        return "AppUser already exists with that email";
    }
}
