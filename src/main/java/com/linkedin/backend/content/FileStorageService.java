package com.linkedin.backend.content;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    public FileStorageService(FileStorageConfig fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
    }

    public void deleteFile(String fileName) throws FileStorageException {
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.delete(targetLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file");
        }

    }

    public String storeFile(MultipartFile file) throws FileStorageException{
        String fileName = UUID.randomUUID().toString();

        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file");
        }

        return fileName;
    }

    public Resource loadFileAsResource(String fileName) throws FileNotFoundException {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists())
                return resource;
            else
                throw new FileNotFoundException("File " + fileName + " could not be found");

        } catch (MalformedURLException e) {
            throw new FileNotFoundException("File " + fileName + " could not be found");
        }
    }

    public byte[] loadFileAsByteArray(String fileName) throws FileNotFoundException {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            return FileUtils.readFileToByteArray(new File(filePath.toString()));
        } catch (IOException e) {
            throw new FileNotFoundException("File " + fileName + " could not be found");
        }
    }
}
