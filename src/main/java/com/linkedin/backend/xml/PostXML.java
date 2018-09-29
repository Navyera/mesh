package com.linkedin.backend.xml;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkedin.backend.entities.post.Post;
import com.linkedin.backend.entities.post.PostType;

import java.util.Date;

public class PostXML {
    private Integer id;

    private PostType type;

    private String body;

    private Integer fileRef;

    private Date date;

    public PostXML(Post post) {
        id = post.getId();
        type = post.getType();
        body = post.getBody();
        if(post.getFile() != null)
            fileRef = post.getFile().getId();

        date = post.getDate();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PostType getType() {
        return type;
    }

    public void setType(PostType type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getFileRef() {
        return fileRef;
    }

    public void setFileRef(Integer fileRef) {
        this.fileRef = fileRef;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
