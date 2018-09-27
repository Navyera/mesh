package com.linkedin.backend.admin.xml;

import com.linkedin.backend.post.Like;

import java.util.Date;

public class LikeXML {
    private Integer postId;
    private Date date;

    public LikeXML(Like like) {
        postId = like.getPost().getId();
        date = like.getDate();
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
