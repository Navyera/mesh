package com.linkedin.backend.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.linkedin.backend.dto.UserUpdateDTO;
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
    public RegisterModel getSettings(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());
        return user.toRegisterModel();
    }

    @PutMapping("/settings/user_details")
    public void updateSettings(@Valid @RequestHeader(value="Authorization") String auth, @RequestBody UserUpdateDTO userUpdateDTO) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        user.setFirstName(userUpdateDTO.getFirstName());
        user.setLastName(userUpdateDTO.getLastName());
        user.setPhone(userUpdateDTO.getPhone().toString());

        appUserService.addUser(user);
    }

    @PutMapping("/settings/email")
    public void updateEmail(@Valid @RequestHeader(value="Authorization") String auth, @RequestBody UserUpdateDTO userUpdateDTO) throws UserNotFoundException, DuplicateUserException{
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        if (appUserService.findUserByEmail(userUpdateDTO.getEmail()) != null) {
            throw new DuplicateUserException();
        }

        user.setEmail(userUpdateDTO.getEmail());

        appUserService.addUser(user);
    }

    @PostMapping("/settings/password")
    public void updatePassword(@Valid @RequestHeader(value="Authorization") String auth, @RequestBody UserUpdateDTO userUpdateDTO) throws UserNotFoundException, PasswordMismatchException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        if (!bCryptPasswordEncoder.matches(userUpdateDTO.getOldPassword(), user.getPassword()))
            throw new PasswordMismatchException(user.getId());

        user.setPassword(bCryptPasswordEncoder.encode(userUpdateDTO.getNewPassword()));

        appUserService.addUser(user);
    }
}
