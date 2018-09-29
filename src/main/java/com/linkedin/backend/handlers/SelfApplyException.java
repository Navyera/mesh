package com.linkedin.backend.handlers;

public class SelfApplyException extends Exception {
    @Override
    public String getMessage() {
        return "You cannot apply to your own job postings.";
    }
}
