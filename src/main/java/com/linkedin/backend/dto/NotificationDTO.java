package com.linkedin.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkedin.backend.post.Comment;
import com.linkedin.backend.post.Like;

import java.util.Date;

public class NotificationDTO {
    private Integer userID;
    private Integer postID;

    private String firstName;
    private String lastName;

    private String type;

    private Date date;

    public NotificationDTO() {

    }

    public NotificationDTO(Comment comment) {
        postID    = comment.getCommentId();
        userID    = comment.getUser().getId();
        firstName = comment.getUser().getFirstName();
        lastName  = comment.getUser().getLastName();
        date      = comment.getDate();
        type      = "comment";
    }

    public NotificationDTO(Like like) {
        postID    = like.getId().getPostId();
        userID    = like.getId().getUserId();
        firstName = like.getUser().getFirstName();
        lastName  = like.getUser().getLastName();
        date      = like.getDate();
        type      = "like";
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
