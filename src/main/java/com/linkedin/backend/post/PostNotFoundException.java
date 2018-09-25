package com.linkedin.backend.post;

public class PostNotFoundException extends Exception {
    private Integer postId;

    public PostNotFoundException(Integer postId) {
        this.postId = postId;
    }

    @Override
    public String getMessage() {
        return "Post with id " + postId + " not found.";
    }
}
