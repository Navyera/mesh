package com.linkedin.backend.conversation;

public class ConversationNotFound extends Exception {
    private Integer id;

    public ConversationNotFound(Integer id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Conversation with ID " + id + " not found.";
    }
}
