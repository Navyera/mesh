package com.linkedin.backend.content;

import com.linkedin.backend.user.AppUser;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.UserNotFoundException;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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
    public Integer uploadFile(@RequestParam("file")MultipartFile file) throws FileStorageException, UserNotFoundException {
        String fileName = fileStorageService.storeFile(file);

        AppUser user = appUserService.findUserById(1);
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
}
