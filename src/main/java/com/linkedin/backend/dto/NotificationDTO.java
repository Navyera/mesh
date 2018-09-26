package com.linkedin.backend.dto;

import com.linkedin.backend.post.Comment;

public class NotificationDTO {
    private Integer userID;
    private Integer postID;

    private String type;

    public NotificationDTO() {

    }

    public NotificationDTO(Comment comment) {

    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
