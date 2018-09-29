package com.linkedin.backend.entities.connection;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ConnectionId implements Serializable {
    @Column(name = "requester_id")
    private Integer requesterId;

    @Column(name = "receiver_id")
    private Integer receiverId;

    private ConnectionId() {
    }

    public ConnectionId(Integer requesterId, Integer receiverId) {
        this.requesterId = requesterId;
        this.receiverId = receiverId;
    }

    public Integer getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Integer requesterId) {
        this.requesterId = requesterId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requesterId, receiverId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        ConnectionId that = (ConnectionId) obj;

        return Objects.equals(receiverId, that.receiverId) &&
               Objects.equals(requesterId, that.requesterId);
    }
}
