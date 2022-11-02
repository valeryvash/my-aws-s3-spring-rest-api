package net.valeryvash.myawss3springrestapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.valeryvash.myawss3springrestapi.model.File;
import net.valeryvash.myawss3springrestapi.repository.FileRepo;
import net.valeryvash.myawss3springrestapi.service.FileService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    // FIXME add params check and exceptions throw
    private final FileRepo fileRepo;

    public FileServiceImpl(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }

    @Override
    public File addFile(File file) {
        try {
            File result = fileRepo.save(file);
            log.info("File info saved successfully {} {}", result.getId(), result.getFileName());
            return result;
        } catch (RuntimeException e) {
            log.warn("File info save exception occurred: {}", e.getMessage());
            throw e;
        }
    }


}
