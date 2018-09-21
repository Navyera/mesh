package com.linkedin.backend.content;

import com.linkedin.backend.user.AppUser;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}
