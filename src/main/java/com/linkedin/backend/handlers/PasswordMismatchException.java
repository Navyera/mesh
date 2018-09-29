package com.linkedin.backend.handlers;

public class PasswordMismatchException extends Exception{
    private Integer id;
    public PasswordMismatchException(Integer id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Old password for user " + id.toString() + " is invalid.";
    }
}
