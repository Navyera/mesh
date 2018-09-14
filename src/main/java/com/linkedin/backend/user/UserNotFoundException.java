package com.linkedin.backend.user;

public class UserNotFoundException extends Exception {
    Integer id;

    public UserNotFoundException(Integer id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "No user with id " + id.toString() + " found.";
    }
}