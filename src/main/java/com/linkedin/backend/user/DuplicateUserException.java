package com.linkedin.backend.user;

public class DuplicateUserException extends Exception {
    @Override
    public String getMessage() {
        return "User already exists with that email";
    }
}