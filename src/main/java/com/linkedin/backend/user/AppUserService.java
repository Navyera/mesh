package com.linkedin.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public AppUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(AppUser appUser) {
        userRepository.save(appUser);
    }

    public AppUser findUserById(Integer id) throws UserNotFoundException{
        return userRepository.findById(id)
                             .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByEmail(username);

        if (appUser == null)
            throw new UsernameNotFoundException(username);

        return new User(appUser.getEmail(), appUser.getPassword(), appUser.getAuthority());
    }

    public AppUser findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
