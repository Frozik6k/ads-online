package ru.skypro.homework.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ImageService {

    byte[] getImage(String id);

    String addImage(MultipartFile file);

    void updateImage(String uuid, MultipartFile file);
}
