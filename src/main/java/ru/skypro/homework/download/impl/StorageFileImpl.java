package ru.skypro.homework.download.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import ru.skypro.homework.download.StorageFile;

@Component
public class StorageFileImpl implements StorageFile {

    @Value("${upload.path}")
    private String path;

    @Override
    public String download(MultipartFile multipartFile) throws IOException {

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
        return filePath.toString();
    }

    @Override
    public byte[] upload(String path) throws IOException {
        Path uploadDir = Paths.get(path);
        return Files.readAllBytes(uploadDir);
    }

    @Override
    public void delete(String path) {
        Path uploadDir = Paths.get(path);
        uploadDir.toFile().delete();
    }
}
