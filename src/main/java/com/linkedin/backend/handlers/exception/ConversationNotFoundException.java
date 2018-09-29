package com.linkedin.backend.handlers.exception;

public class ConversationNotFoundException extends Exception {
    private Integer id;

    public ConversationNotFoundException(Integer id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Conversation with ID " + id + " not found.";
    }
}
