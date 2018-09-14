package com.linkedin.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public User findUserById(Integer id) throws UserNotFoundException{
        return userRepository.findById(id)
                             .orElseThrow(() -> new UserNotFoundException(id));
    }
}
