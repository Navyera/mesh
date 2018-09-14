package com.linkedin.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public void addUser(AppUser appUser) {
        appUserRepository.save(appUser);
    }

    public AppUser findUserById(Integer id) throws UserNotFoundException{
        return appUserRepository.findById(id)
                             .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(username);

        if (appUser == null)
            throw new UsernameNotFoundException(username);

        return new User(appUser.getEmail(), appUser.getPassword(), Collections.emptyList());
    }
}
