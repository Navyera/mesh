package com.linkedin.backend.content;

import com.linkedin.backend.user.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ContentController {
    final private FileStorageService fileStorageService;

    @Autowired
    public ContentController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/api/upload")
    public String uploadFile(@RequestParam("file")MultipartFile file) throws FileStorageException {
        String fileName = fileStorageService.storeFile(file);

        return fileName;
    }
}
