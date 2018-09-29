package com.linkedin.backend.xml;

public class ConnectionXML {
    private Integer userId;

    public ConnectionXML(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
