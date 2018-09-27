package com.linkedin.backend.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.linkedin.backend.admin.xml.AdminXML;
import com.linkedin.backend.admin.xml.AppUserXML;
import com.linkedin.backend.dto.ProfileDTO;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/users")
    public List<ProfileDTO> getUserList(@Valid @RequestHeader(value="Authorization") String auth) {
        JWTUtils token = new JWTUtils(auth);
        Integer myId = token.getUserID();

        return appUserService.getAll(myId).stream().map(ProfileDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/xml")
    public String getXml(@Valid @RequestHeader(value="Authorization") String auth) throws JsonProcessingException {
        JWTUtils token = new JWTUtils(auth);
        Integer myId = token.getUserID();

        List<AppUserXML> users = appUserService.getAll(myId).stream().map(AppUserXML::new).collect(Collectors.toList());

        XmlMapper mapper = new XmlMapper();

        return mapper.writeValueAsString(new AdminXML(users));
    }
}
