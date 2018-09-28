package com.linkedin.backend.user;

import com.linkedin.backend.user.dao.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<AppUser, Integer> {
    AppUser findByEmail(String username);
    List<AppUser> findDistinctByFirstNameContainingOrLastNameContainingAllIgnoreCase(String firstName, String lastName);
}
