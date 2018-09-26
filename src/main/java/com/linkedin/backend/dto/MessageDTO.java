package com.linkedin.backend.dto;

import com.linkedin.backend.message.Message;

import java.util.Date;

public class MessageDTO {
    private String body;

    private Date date;

    private Boolean mine;

    public MessageDTO() {
    }

    public MessageDTO(Message message, Integer myId) {
        body = message.getBody();
        date = message.getTime();
        mine = message.getSender().getId().equals(myId);
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

    public Boolean getMine() {
        return mine;
    }

    public void setMine(Boolean mine) {
        this.mine = mine;
    }
}
