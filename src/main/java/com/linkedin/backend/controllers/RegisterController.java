package com.linkedin.backend.controllers;

import com.linkedin.backend.models.RegisterModel;
import com.linkedin.backend.post.Like;
import com.linkedin.backend.user.*;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.dao.Skill;
import com.linkedin.backend.handlers.DuplicateUserException;
import com.linkedin.backend.handlers.UserNotFoundException;
import com.linkedin.backend.utils.JSONStatus;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public JSONStatus register(@Valid @RequestBody RegisterModel model) throws DuplicateUserException {
        // Hash and salt the password using BCrypt encoder.
        model.setPassword(bCryptPasswordEncoder.encode(model.getPassword()));

        try {
            appUserService.addUser(model.toUser());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new DuplicateUserException();
        }

        return new JSONStatus("Registration successful");
    }






    // TODO: This is a test controller
    @PostMapping("/test")
    public void test(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        AppUser user2 = appUserService.findUserById(9);

        user.addConnection(user2);

        appUserService.addUser(user);
    }




    // TODO: This is a test controller
    @GetMapping("/{id}")
    public List<Like> getUser(@Valid @PathVariable int id) throws UserNotFoundException {
        AppUser user = appUserService.findUserById(id);

        Skill skill = new Skill();

        skill.setSkillDescription("sdafasdfas");
        return user.getLikes();
    }
}
