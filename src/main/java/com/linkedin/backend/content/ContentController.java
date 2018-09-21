package com.linkedin.backend.content;

import com.linkedin.backend.dto.ImageResourceDTO;
import com.linkedin.backend.user.AppUser;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.Profile;
import com.linkedin.backend.user.UserNotFoundException;
import com.linkedin.backend.utils.JWTUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;
import java.util.*;

@RestController
public class ContentController {
    final private FileStorageService fileStorageService;
    final private ContentService contentService;
    final private AppUserService appUserService;

    @Autowired
    public ContentController(FileStorageService fileStorageService, AppUserService appUserService, ContentService contentService) {
        this.fileStorageService = fileStorageService;
        this.appUserService = appUserService;
        this.contentService = contentService;
    }

    @PostMapping("/api/upload")
    public Integer uploadFile(@RequestParam("file")MultipartFile file, @Valid @RequestHeader(value="Authorization") String auth) throws FileStorageException, UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        String fileName = fileStorageService.storeFile(file);

        File fileMetadata = new File();

        fileMetadata.setContentId(fileName);
        fileMetadata.setOwner(user);
        fileMetadata.setContentLength(file.getSize());
        fileMetadata.setMimeType(file.getContentType());

        contentService.addFile(fileMetadata);

        return fileMetadata.getId();
    }

    @PostMapping("/api/upload/profile_picture")
    public AppUser uploadProfilePicture(@RequestParam("file") MultipartFile picture, @Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException, FileStorageException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        // Store file and get the random generated name.
        String fileName = fileStorageService.storeFile(picture);

        // Insert metadata about the file in the database.
        File fileMetadata = new File();

        fileMetadata.setContentId(fileName);
        fileMetadata.setOwner(user);
        fileMetadata.setContentLength(picture.getSize());
        fileMetadata.setMimeType(picture.getContentType());

        contentService.addFile(fileMetadata);

        // Update the profile picture reference.
        user.getProfile().setProfilePicture(fileMetadata);

        appUserService.addUser(user);

        return user;
    }

    @GetMapping("/api/content/profile_picture")
    public ImageResourceDTO getProfilePicture(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException, FileNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        File userProfilePicture = user.getProfile().getProfilePicture();

        if (userProfilePicture == null)
            return null;

        byte[] fileContent = fileStorageService.loadFileAsByteArray(userProfilePicture.getContentId());

        return new ImageResourceDTO(userProfilePicture.getMimeType(), Base64.getEncoder().encodeToString(fileContent));
    }
}
