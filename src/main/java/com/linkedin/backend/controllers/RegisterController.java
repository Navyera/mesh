package com.linkedin.backend.controllers;

import com.linkedin.backend.dto.RegisterDTO;
import com.linkedin.backend.entities.user.AppUserService;
import com.linkedin.backend.handlers.exception.DuplicateUserException;
import com.linkedin.backend.utils.JSONStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class RegisterController {
    final private AppUserService appUserService;
    final private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public RegisterController(AppUserService appUserService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserService = appUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/register")
    public JSONStatus register(@Valid @RequestBody RegisterDTO model) throws DuplicateUserException {
        // Hash and salt the password using BCrypt encoder.
        model.setPassword(bCryptPasswordEncoder.encode(model.getPassword()));

        try {
            appUserService.addUser(model.toUser());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new DuplicateUserException();
        }

        return new JSONStatus("Registration successful");
    }
}
