package com.linkedin.backend.xml;

import com.linkedin.backend.entities.comment.Comment;

import java.util.Date;

public class CommentXML {
    private Integer commentId;

    private Integer postId;

    private String body;

    private Date date;

    public CommentXML(Comment comment) {
        commentId = comment.getCommentId();
        postId = comment.getPost().getId();
        body = comment.getBody();
        date = comment.getDate();
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
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
