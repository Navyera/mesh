package com.linkedin.backend.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.linkedin.backend.admin.xml.AdminXML;
import com.linkedin.backend.admin.xml.AppUserXML;
import com.linkedin.backend.dto.ProfileDTO;
import com.linkedin.backend.dto.UserListItem;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.handlers.UserNotFoundException;
import com.linkedin.backend.utils.JWTUtils;
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
    public List<UserListItem> getUserList() {
        return appUserService.getAll().stream().map(UserListItem::new).collect(Collectors.toList());
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
