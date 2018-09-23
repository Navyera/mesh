package com.linkedin.backend.user.controllers;

import com.linkedin.backend.dto.ProfileDTO;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.handlers.UserNotFoundException;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users/profile")
public class ProfileController {
    final private AppUserService appUserService;

    @Autowired
    public ProfileController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }


    @GetMapping("")
    public ProfileDTO getMyProfile(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        return user.toProfileDTO();
    }

    @PostMapping("")
    public void updateMyProfile(@Valid @RequestHeader(value="Authorization") String auth, @Valid @RequestBody ProfileDTO profileDTO) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        appUserService.clearUserSkills(user);

        user.getProfile().setAbout(profileDTO.getAbout());
        user.getProfile().setEducation(profileDTO.getEducation());
        user.getProfile().setJob(profileDTO.getJob());
        user.setSkillsFromStrList(profileDTO.getSkills());

        appUserService.addUser(user);
    }


    @GetMapping("/{id}")
    public ProfileDTO getUserProfile(@Valid @RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer id) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser currentUser = appUserService.findUserById(token.getUserID());

        AppUser requestedUser = appUserService.findUserById(id);

        // TODO: Check access rights of current user.

        return requestedUser.toProfileDTO();
    }
}
