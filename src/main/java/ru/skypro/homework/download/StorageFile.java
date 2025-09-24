package ru.skypro.homework.download;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
public class StorageFile {
    @Value("${upload.path}")
    private String path;

    public String download(MultipartFile multipartFile) {
        try {
            Path uploadDir = Paths.get(path);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFilename = multipartFile.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID() + extension;

            Path filePath = uploadDir.resolve(filename);
            multipartFile.transferTo(filePath);
            log.info("File uploaded successfully: {}", filePath);
            return filePath.toString();
        } catch (IOException exception) {
            throw new RuntimeException("Не удалось сохранить файл", exception);
        }
    }
}
