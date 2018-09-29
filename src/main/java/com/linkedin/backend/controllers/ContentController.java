package com.linkedin.backend.controllers;

import com.linkedin.backend.content.*;
import com.linkedin.backend.dto.ImageResourceDTO;
import com.linkedin.backend.handlers.exception.FileNotFoundException;
import com.linkedin.backend.handlers.exception.FileStorageException;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.handlers.exception.UserNotFoundException;
import com.linkedin.backend.utils.JSONStatus;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Base64;

import static org.springframework.http.ResponseEntity.ok;

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

    @PostMapping("/api/content/profile_picture")
    public JSONStatus uploadProfilePicture(@RequestParam("file") MultipartFile picture, @Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException, FileStorageException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        File profilePicture = user.getProfile().getProfilePicture();
        File fileMetadata;

        if (profilePicture != null) {
            fileMetadata = profilePicture;
            this.fileStorageService.deleteFile(profilePicture.getContentId());
        }
        else {
            fileMetadata = new File();
        }
        // Store file and get the random generated name.
        String fileName = fileStorageService.storeFile(picture);

        // Insert metadata about the file in the database.


        fileMetadata.setContentId(fileName);
        fileMetadata.setOwner(user);
        fileMetadata.setContentLength(picture.getSize());
        fileMetadata.setMimeType(picture.getContentType());

        contentService.addFile(fileMetadata);

        // Update the profile picture reference.
        user.getProfile().setProfilePicture(fileMetadata);

        appUserService.addUser(user);

        return new JSONStatus("OK");
    }

    @GetMapping("/api/content/{id}")
    public ImageResourceDTO getContent(@Valid @PathVariable Integer id) throws FileNotFoundException {
        File file = contentService.findFileById(id);

        byte[] fileContent = fileStorageService.loadFileAsByteArray(file.getContentId());

        return new ImageResourceDTO(file.getMimeType(), Base64.getEncoder().encodeToString(fileContent));
    }

    @GetMapping("/api/content/test/{id}")
    public ResponseEntity<Resource> test(@Valid @PathVariable Integer id) throws FileNotFoundException {
        File file = contentService.findFileById(id);

        Resource fileContent = fileStorageService.loadFileAsResource(file.getContentId());

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getMimeType())).body(fileContent);
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

    @GetMapping("/api/content/profile_picture/{id}")
    public ImageResourceDTO getProfilePicture(@Valid @PathVariable Integer id) throws UserNotFoundException, FileNotFoundException {
        AppUser user = appUserService.findUserById(id);

        File userProfilePicture = user.getProfile().getProfilePicture();

        if (userProfilePicture == null)
            return null;

        byte[] fileContent = fileStorageService.loadFileAsByteArray(userProfilePicture.getContentId());

        return new ImageResourceDTO(userProfilePicture.getMimeType(), Base64.getEncoder().encodeToString(fileContent));
    }
}
