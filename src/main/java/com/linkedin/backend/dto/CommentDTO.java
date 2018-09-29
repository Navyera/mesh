package com.linkedin.backend.dto;

import com.linkedin.backend.entities.comment.Comment;

import java.util.Date;

public class CommentDTO {
    private Integer userID;

    private String body;

    private Date date;

    public CommentDTO() {
    }

    public CommentDTO(Comment comment) {
        userID = comment.getUser().getId();
        body   = comment.getBody();
        date   = comment.getDate();
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
