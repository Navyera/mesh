package com.linkedin.backend.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {
    private final FileRepository fileRepository;

    @Autowired
    public ContentService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void addFile(File file) {
        fileRepository.save(file);
    }

    public File findFileById(Integer id) throws FileNotFoundException {
        return fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException(id.toString()));
    }
}
