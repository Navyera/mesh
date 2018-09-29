package com.linkedin.backend.entities.like;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linkedin.backend.entities.post.Post;
import com.linkedin.backend.entities.user.AppUser;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "user_likes_post")
public class Like {
    @EmbeddedId
    private LikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JsonIgnore
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @MapsId("postId")
    @JsonIgnore
    private Post post;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    private Like() {

    }

    public Like(AppUser user, Post post) {
        this.user = user;
        this.post = post;
        this.id = new LikeId(user.getId(), post.getId());
    }

    public LikeId getId() {
        return id;
    }

    public void setId(LikeId id) {
        this.id = id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, post);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Like that = (Like) obj;

        return Objects.equals(user, that.user) &&
                Objects.equals(post, that.post);
    }
}
