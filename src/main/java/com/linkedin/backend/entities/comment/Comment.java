package com.linkedin.backend.entities.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.linkedin.backend.entities.post.Post;
import com.linkedin.backend.entities.user.AppUser;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_comments_post")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private Post post;

    private String body;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    public Comment() {
    }

    public Comment(AppUser user, Post post, String body) {
        this.user = user;
        this.post = post;
        this.body = body;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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
