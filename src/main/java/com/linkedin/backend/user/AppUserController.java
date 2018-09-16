package com.linkedin.backend.user;

import com.linkedin.backend.models.RegisterModel;
import com.linkedin.backend.utils.JSONStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AppUserController {
    final private AppUserService appUserService;

    final private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AppUserController(AppUserService appUserService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserService = appUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/api/users/register")
    public JSONStatus register(@Valid @RequestBody RegisterModel model) throws DuplicateUserException {
        // Hash and salt the password using BCrypt encoder.
        model.setPassword(bCryptPasswordEncoder.encode(model.getPassword()));

        try {
            appUserService.addUser(model.toUser());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new DuplicateUserException();
        }

        return new JSONStatus("Registration succesful");
    }

    @GetMapping("/api/users/{id}")
    public AppUser getUser(@Valid @PathVariable int id) throws UserNotFoundException {
        return appUserService.findUserById(id);
    }
}
