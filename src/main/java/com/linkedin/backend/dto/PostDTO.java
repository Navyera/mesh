package com.linkedin.backend.dto;

import com.linkedin.backend.post.Post;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PostDTO {
    private Integer userId;

    private String type;

    private String body;

    private Integer fileId;

    private Date date;

    private List<CommentDTO> comments;

    public PostDTO() {
    }

    public PostDTO(Post post) {
        userId = post.getUser().getId();
        type   = post.getType().name().toLowerCase();
        body   = post.getBody();
        date   = post.getDate();
        comments = post.getComments().stream().map(CommentDTO::new).collect(Collectors.toList());
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }
}
