package com.linkedin.backend.dto;

import com.linkedin.backend.post.Post;
import com.linkedin.backend.user.dao.AppUser;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PostDTO {
    private Integer postID;

    private Integer userID;

    private String type;

    private String body;

    private Integer fileID;

    private Date date;

    private List<Integer> userLikes;

    private List<CommentDTO> comments;

    private boolean liked = false;

    public PostDTO() {
    }

    public PostDTO(Post post, AppUser user) {
        postID = post.getId();
        userID = post.getUser().getId();
        body   = post.getBody();

        date   = post.getDate();
        userLikes = post.getUsers().stream().map(AppUser::getId).collect(Collectors.toList());
        comments = post.getComments().stream().map(CommentDTO::new).collect(Collectors.toList());
        liked = userLikes.contains(user.getId());

        if (post.getFile() != null) {
            fileID = post.getId();
            type   = post.getFile().getMimeType();
        }
    }

    public PostDTO(Post post) {
        postID = post.getId();
        userID = post.getUser().getId();
        type   = post.getType().name().toLowerCase();
        body   = post.getBody();
        date   = post.getDate();
        userLikes = post.getUsers().stream().map(AppUser::getId).collect(Collectors.toList());
        comments = post.getComments().stream().map(CommentDTO::new).collect(Collectors.toList());

        if (post.getFile() != null) {
            fileID = post.getId();
            type   = post.getFile().getMimeType();
        }
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
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

    public Integer getFileID() {
        return fileID;
    }

    public void setFileID(Integer fileID) {
        this.fileID = fileID;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    public List<Integer> getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(List<Integer> userLikes) {
        this.userLikes = userLikes;
    }

    public boolean getLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}
