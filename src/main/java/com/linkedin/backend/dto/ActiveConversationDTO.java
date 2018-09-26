package com.linkedin.backend.dto;

import com.linkedin.backend.conversation.Conversation;
import com.linkedin.backend.user.dao.AppUser;

import java.util.Date;

public class ActiveConversationDTO {
    private Integer conversationID;

    private String firstName;
    private String lastName;

    private Integer userID;

    private Date date;

    public ActiveConversationDTO(Conversation conversation, boolean small) {
        conversationID = conversation.getConversationId();
        date = conversation.getLastUpdated();

        AppUser targetUser = small ? conversation.getLargerUser() : conversation.getSmallerUser();

        firstName = targetUser.getFirstName();
        lastName  = targetUser.getLastName();
        userID    = targetUser.getId();
    }

    public Integer getConversationID() {
        return conversationID;
    }

    public void setConversationID(Integer conversationID) {
        this.conversationID = conversationID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
