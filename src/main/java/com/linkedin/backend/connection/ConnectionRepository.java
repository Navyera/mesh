package com.linkedin.backend.connection;

import com.linkedin.backend.user.dao.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface ConnectionRepository extends CrudRepository<Connection, ConnectionId> {

}
