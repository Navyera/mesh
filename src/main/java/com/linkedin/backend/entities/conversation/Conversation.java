package com.linkedin.backend.entities.conversation;

import com.linkedin.backend.entities.message.Message;
import com.linkedin.backend.entities.user.AppUser;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id")
    private Integer conversationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "smaller_id")
    private AppUser smallerUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "larger_id")
    private AppUser largerUser;

    @OneToMany(
            mappedBy = "conversation",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @OrderBy("time ASC")
    private List<Message> messages;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated = new Date();

    public Conversation() {
    }

    public Conversation(AppUser smallerUser, AppUser largerUser) {
        this.smallerUser = smallerUser;
        this.largerUser = largerUser;
    }

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

    public AppUser getSmallerUser() {
        return smallerUser;
    }

    public void setSmallerUser(AppUser smallerUser) {
        this.smallerUser = smallerUser;
    }

    public AppUser getLargerUser() {
        return largerUser;
    }

    public void setLargerUser(AppUser largerUser) {
        this.largerUser = largerUser;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
