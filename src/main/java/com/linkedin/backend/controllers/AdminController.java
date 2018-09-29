package com.linkedin.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.linkedin.backend.dto.ProfileDTO;
import com.linkedin.backend.dto.UserListItemDTO;
import com.linkedin.backend.entities.user.AppUserService;
import com.linkedin.backend.handlers.exception.UserNotFoundException;
import com.linkedin.backend.xml.AdminXML;
import com.linkedin.backend.xml.AppUserXML;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AppUserService appUserService;

    public AdminController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/users/{id}")
    public ProfileDTO getUserInfo(@Valid @PathVariable Integer id) throws UserNotFoundException {
        return new ProfileDTO(appUserService.findUserById(id), false);
    }

    @GetMapping("/users")
    public List<UserListItemDTO> getUserList() {
        return appUserService.getAll().stream().map(UserListItemDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/xml")
    public ResponseEntity<String> getXml(@Valid @RequestHeader(value="Authorization") String auth, @Valid @RequestBody List<Integer> userIds) throws JsonProcessingException {
        List<AppUserXML> users = appUserService.getAll(userIds).stream().map(AppUserXML::new).collect(Collectors.toList());

        XmlMapper mapper = new XmlMapper();

        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_XML)
                .body(mapper.writeValueAsString(new AdminXML(users)));
    }
}
