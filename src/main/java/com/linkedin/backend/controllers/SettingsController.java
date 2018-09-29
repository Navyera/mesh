package com.linkedin.backend.controllers;


import com.linkedin.backend.dto.UserDetailsDTO;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.handlers.exception.DuplicateUserException;
import com.linkedin.backend.handlers.exception.PasswordMismatchException;
import com.linkedin.backend.handlers.exception.UserNotFoundException;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users/settings")
public class SettingsController {
    final private AppUserService appUserService;
    final private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SettingsController(AppUserService appUserService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserService = appUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @GetMapping("")
    public UserDetailsDTO getSettings(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());
        return user.toUserDetails();
    }

    @PutMapping("/user_details")
    public void updateSettings(@Valid @RequestHeader(value="Authorization") String auth, @RequestBody UserDetailsDTO userDetailsDTO) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        user.setFirstName(userDetailsDTO.getFirstName());
        user.setLastName(userDetailsDTO.getLastName());
        user.setPhone(userDetailsDTO.getPhone());

        appUserService.addUser(user);
    }

    @PutMapping("/email")
    public void updateEmail(@Valid @RequestHeader(value="Authorization") String auth, @RequestBody UserDetailsDTO userDetailsDTO) throws UserNotFoundException, DuplicateUserException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        if (appUserService.findUserByEmail(userDetailsDTO.getEmail()) != null) {
            throw new DuplicateUserException();
        }

        user.setEmail(userDetailsDTO.getEmail());

        appUserService.addUser(user);
    }

    @PostMapping("/password")
    public void updatePassword(@Valid @RequestHeader(value="Authorization") String auth, @RequestBody UserDetailsDTO userDetailsDTO) throws UserNotFoundException, PasswordMismatchException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        if (!bCryptPasswordEncoder.matches(userDetailsDTO.getOldPassword(), user.getPassword()))
            throw new PasswordMismatchException(user.getId());

        user.setPassword(bCryptPasswordEncoder.encode(userDetailsDTO.getNewPassword()));

        appUserService.addUser(user);
    }

}
