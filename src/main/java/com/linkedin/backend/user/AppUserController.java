package com.linkedin.backend.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.linkedin.backend.models.RegisterModel;
import com.linkedin.backend.utils.JSONStatus;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class AppUserController {
    final private AppUserService appUserService;
    final private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public AppUserController(AppUserService appUserService, BCryptPasswordEncoder bCryptPasswordEncoder) {
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

        return new JSONStatus("Registration succesful");
    }

    @GetMapping("/{id}")
    public AppUser getUser(@Valid @PathVariable int id) throws UserNotFoundException {
        return appUserService.findUserById(id);
    }

    @GetMapping("/settings")
    public RegisterModel getSettings(@Valid @RequestHeader (value = "Authorization") String auth) throws UserNotFoundException {

        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());
        return user.toRegisterModel();
    }
}
