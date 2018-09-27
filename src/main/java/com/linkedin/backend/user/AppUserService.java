package com.linkedin.backend.user;

import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.handlers.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final SkillsRepository skillsRepository;

    @Autowired
    public AppUserService(UserRepository userRepository, SkillsRepository skillsRepository) {
        this.userRepository = userRepository;
        this.skillsRepository = skillsRepository;
    }

    public void addUser(AppUser appUser) {
        userRepository.save(appUser);
    }

    public AppUser findUserById(Integer id) throws UserNotFoundException {
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

    public void clearUserSkills(AppUser user) {
        skillsRepository.deleteSkillsByUser(user);
    }

    public List<AppUser> getAll(Integer myId) {
        List<AppUser> users = new ArrayList<>();

        for (AppUser user : userRepository.findAll()) {
            if (!user.getId().equals(myId))
                users.add(user);
        }

        return users;
    }
}
