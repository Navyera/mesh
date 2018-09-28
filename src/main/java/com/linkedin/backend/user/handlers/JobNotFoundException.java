package com.linkedin.backend.user.handlers;

public class JobNotFoundException extends Exception {
    private Integer postId;

    public JobNotFoundException(Integer postId) {
        this.postId = postId;
    }

    @Override
    public String getMessage() {
        return "Post with id " + postId + " not found.";
    }
}
