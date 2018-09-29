package com.linkedin.backend.handlers.exception;

public class UserNotFoundException extends Exception {
    private Integer id;

    public UserNotFoundException(Integer id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "No user with id " + id.toString() + " found.";
    }
}
