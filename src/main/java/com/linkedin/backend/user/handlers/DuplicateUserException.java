package com.linkedin.backend.user.handlers;

public class DuplicateUserException extends Exception {
    @Override
    public String getMessage() {
        return "AppUser already exists with that email";
    }
}