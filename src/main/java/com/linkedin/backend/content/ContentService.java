package com.linkedin.backend.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {
    private final FileRepository fileRepostiory;

    @Autowired
    public ContentService(FileRepository fileRepository) {
        this.fileRepostiory = fileRepository;
    }

    public void addFile(File file) {
        fileRepostiory.save(file);
    }

    public File findFileById(Integer id) throws FileNotFoundException {
        return fileRepostiory.findById(id)
                .orElseThrow(() -> new FileNotFoundException(id.toString()));
    }
}
