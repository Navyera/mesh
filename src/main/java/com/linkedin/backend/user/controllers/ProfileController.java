package com.linkedin.backend.user.controllers;

import com.linkedin.backend.connection.ConnectionService;
import com.linkedin.backend.dto.ProfileDTO;
import com.linkedin.backend.dto.ProfileStatsDTO;
import com.linkedin.backend.dto.ProfileViewDTO;
import com.linkedin.backend.user.SkillService;
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
    final private ConnectionService connectionService;
    final private SkillService skillService;

    @Autowired
    public ProfileController(AppUserService appUserService, ConnectionService connectionService, SkillService skillService) {
        this.appUserService = appUserService;
        this.connectionService = connectionService;
        this.skillService = skillService;
    }


    @GetMapping("")
    public ProfileViewDTO getMyProfile(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        return user.toProfileViewDTO();
    }

    @PostMapping("")
    public void updateMyProfile(@Valid @RequestHeader(value="Authorization") String auth, @Valid @RequestBody ProfileViewDTO profileViewDTO) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        //appUserService.clearUserSkills(user);

        ProfileDTO profileDTO = profileViewDTO.getProfileDTO();

        user.getProfile().setAbout(profileDTO.getAbout());
        user.getProfile().setEducation(profileDTO.getEducation());
        user.getProfile().setJob(profileDTO.getJob());
        user.getProfile().setPermissions(profileViewDTO.getPermissionsDTO().getPermissions());

        if (profileDTO.getSkills() != null)
            skillService.setSkillsFromStrList(user, profileDTO.getSkills());

        appUserService.addUser(user);
    }


    @GetMapping("/{id}")
    public ProfileViewDTO getUserProfile(@Valid @RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer id) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        Integer requesterID = token.getUserID();

        AppUser requestedUser = appUserService.findUserById(id);

        ProfileViewDTO requestedProfile = requestedUser.toProfileViewDTO();

        if (connectionService.friends(requesterID, requestedUser.getId()))
            requestedProfile.getPermissionsDTO().setFriend();
        else
            requestedProfile.hidePrivateDetails();


        return requestedProfile;
    }

    @GetMapping("/light/{id}")
    public ProfileDTO getUserName(@Valid @RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer id) throws UserNotFoundException {
        return getUserProfile(auth, id).stripDetails().getProfileDTO();
    }

    @GetMapping("/stats")
    public ProfileStatsDTO getUserStats(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        return new ProfileStatsDTO(user);
    }
}
