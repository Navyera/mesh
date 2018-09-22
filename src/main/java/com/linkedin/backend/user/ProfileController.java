package com.linkedin.backend.user;

import com.linkedin.backend.dto.ProfileDTO;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController()
@RequestMapping("/api/users/profile")
public class ProfileController {
    final private AppUserService appUserService;

    @Autowired
    public ProfileController(AppUserService appUserService) {
        this.appUserService = appUserService;
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
