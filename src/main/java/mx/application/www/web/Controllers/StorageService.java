package mx.application.www.web.Controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

    private Path rootLocation;
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    public StorageService() {
        this.rootLocation = (new File(System.getProperty("java.io.tmpdir"))).toPath();
        LOGGER.debug("Root location: {}", rootLocation.toAbsolutePath().toString());
    }

    public Path store(MultipartFile file) {
        LOGGER.debug("store()");
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        LOGGER.debug("Storing {}", filename);
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new RuntimeException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Path newFile = this.rootLocation.resolve(filename);
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
                return newFile;
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }
}
