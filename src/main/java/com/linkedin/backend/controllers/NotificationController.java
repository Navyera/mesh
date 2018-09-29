package com.linkedin.backend.controllers;

import com.linkedin.backend.dto.NotificationDTO;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.handlers.exception.UserNotFoundException;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/users/notifications")
public class NotificationController {
    private final AppUserService appUserService;

    public NotificationController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("")
    public List<NotificationDTO> getNotifications(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        List<NotificationDTO> notifications = user.getNotifications();
        notifications.sort((Comparator.comparing(NotificationDTO::getDate)).reversed());

        return notifications;
    }
}
