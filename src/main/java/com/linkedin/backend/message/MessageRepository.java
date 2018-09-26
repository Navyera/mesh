package com.linkedin.backend.message;

import com.linkedin.backend.user.dao.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Integer>{
}
