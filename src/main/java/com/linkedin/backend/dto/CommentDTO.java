package com.linkedin.backend.dto;

import com.linkedin.backend.post.Comment;

import java.util.Date;

public class CommentDTO {
    private Integer userId;

    private String body;

    private Date date;

    public CommentDTO() {
    }

    public CommentDTO(Comment comment) {
        userId = comment.getUser().getId();
        body   = comment.getBody();
        date   = comment.getDate();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
