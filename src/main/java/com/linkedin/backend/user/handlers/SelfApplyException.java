package com.linkedin.backend.user.handlers;

public class SelfApplyException extends Exception {
    @Override
    public String getMessage() {
        return "You cannot apply to your own job postings.";
    }
}
