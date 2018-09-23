package com.linkedin.backend.connection;

import com.linkedin.backend.user.dao.AppUser;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Connection {
    @EmbeddedId
    private ConnectionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("requesterId")
    private AppUser requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("receiverId")
    private AppUser receiver;

    private Integer accepted = 0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date time = new Date();

    private Connection() {
    }

    public Connection(AppUser requester, AppUser receiver) {
        this.requester = requester;
        this.receiver = receiver;
        this.id = new ConnectionId(requester.getId(), receiver.getId());
    }

    public ConnectionId getId() {
        return id;
    }

    public void setId(ConnectionId id) {
        this.id = id;
    }

    public AppUser getRequester() {
        return requester;
    }

    public void setRequester(AppUser requester) {
        this.requester = requester;
    }

    public AppUser getReceiver() {
        return receiver;
    }

    public void setReceiver(AppUser receiver) {
        this.receiver = receiver;
    }

    public void setAccepted(Integer accepted) {
        this.accepted = accepted;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requester, receiver);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        Connection that = (Connection) obj;

        return Objects.equals(receiver, that.receiver) &&
               Objects.equals(requester, that.requester);
    }
}
