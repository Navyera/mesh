package com.linkedin.backend.entities.user;

import com.linkedin.backend.entities.skill.SkillRepository;
import com.linkedin.backend.handlers.exception.UserNotFoundException;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppUserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @Autowired
    public AppUserService(UserRepository userRepository, SkillRepository skillRepository) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
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

    public List<AppUser> getAll(List<Integer> userIds) {
        List<AppUser> users = new ArrayList<>();

        for (Integer userId : userIds) {
            try {
                AppUser user = findUserById(userId);

                if (!user.getRole().equals("ROLE_ADMIN"))
                    users.add(user);
            } catch (UserNotFoundException ignored) {

            }
        }

        return users;
    }

    public List<AppUser> getAll() {
        List<AppUser> users = new ArrayList<>();

        for (AppUser user : userRepository.findAll()) {
            if (!user.getRole().equals("ROLE_ADMIN"))
                users.add(user);
        }

        return users;
    }

    private Integer calculateScore(AppUser user, String firstTerm, String secondTerm) {
        Integer scoreA = LevenshteinDistance.getDefaultInstance().apply(user.getFirstName().toLowerCase() +
                                                                            user.getLastName().toLowerCase(),
                                                                        firstTerm + secondTerm);

        Integer scoreB = LevenshteinDistance.getDefaultInstance().apply(user.getFirstName() + user.getLastName(), secondTerm + firstTerm);

        return scoreA > scoreB ? scoreB : scoreA;
    }

    public Iterable<AppUser> findAll() {
        return userRepository.findAll();
    }

    public List<AppUser> getSearchResults(String firstTerm, String lastTerm) {
        List<AppUser> listA = userRepository.findDistinctByFirstNameContainingOrLastNameContainingAllIgnoreCase(firstTerm, lastTerm);
        List<AppUser> listB = userRepository.findDistinctByFirstNameContainingOrLastNameContainingAllIgnoreCase(lastTerm, firstTerm);

        Map<AppUser, Integer> distinctBatch = new HashMap<>();

        ListUtils.union(listA, listB).forEach(user -> distinctBatch.put(user, 0));

        distinctBatch.forEach((user, score) -> distinctBatch.put(user, calculateScore(user, firstTerm, lastTerm)));

        return distinctBatch.entrySet()
                            .stream()
                            .sorted(Comparator.comparing(Map.Entry::getValue))
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList());
    }
}
