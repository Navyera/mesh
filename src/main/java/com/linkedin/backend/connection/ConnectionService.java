package com.linkedin.backend.connection;

import com.linkedin.backend.user.dao.AppUser;
import org.apache.coyote.http2.ConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConnectionService {
    private final ConnectionRepository connectionRepository;

    @Autowired
    public ConnectionService(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    public Boolean friends(Integer user1, Integer user2) {

        Optional<Connection> connection = connectionRepository.findById(new ConnectionId(user1, user2));
        if(connection.isPresent())
            return connection.get().getAccepted() == 1;

        connection = connectionRepository.findById(new ConnectionId(user2, user1));
        if(connection.isPresent())
            return connection.get().getAccepted() == 1;

        return false;
    }

    public Connection findConnectionById(Integer requesterId, Integer receiverId) throws ConnectionNotFoundException {
        Optional<Connection> connection = connectionRepository.findById(new ConnectionId(requesterId, receiverId));
        return connection.orElseThrow(ConnectionNotFoundException::new);

    }

    public void addConnection(AppUser requester, AppUser receiver) throws DuplicateConnectionException {
        if (connectionRepository.existsById(new ConnectionId(requester.getId(), receiver.getId())))
            throw new DuplicateConnectionException();
        connectionRepository.save(new Connection(requester, receiver));
    }

    public void removeConnection(Connection connection) {
        connectionRepository.delete(connection);
    }

    public void activateConnection(Connection connection)  {
        connection.setAccepted(1);
        connectionRepository.save(connection);
    }
}
