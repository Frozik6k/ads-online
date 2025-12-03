package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.download.StorageFile;
import ru.skypro.homework.exception.ImageAccessErrorException;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    final private ImageRepository imageRepository;
    final private StorageFile storageFile;

    @Override
    public byte[] getImage(String id) {
        Image image = imageRepository.findById(id)
                .orElseGet(() -> {
                    log.warn("Картинка с id {} не найдена", id);
                    throw new ImageAccessErrorException(id);
                });

        try {
            return storageFile.upload(image.getPath());
        } catch (IOException e) {
            log.warn("Картинка с id {} не удалось загрузить", id);
            throw new ImageAccessErrorException(id);
        }

    }

    @Override
    public String addImage(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();

        try {
            String path = storageFile.download(file);
            Image image = new Image(uuid, path);
            imageRepository.save(image);
        } catch (IOException e) {
            log.warn("Картинку {} не удалось загрузить в хранилище ", file.getOriginalFilename());
            throw new ImageAccessErrorException(file.getOriginalFilename());
        }

        return uuid;
    }

    @Override
    public void updateImage(String uuid, MultipartFile file) {
        Image image = imageRepository.findById(uuid)
                .orElseGet(() -> new Image(uuid, ""));
        try {
            String path = storageFile.download(file);
            image.setPath(path);
            log.info(this.getClass() + "-> updateImage -> path: {}", path);
            imageRepository.save(image);
        } catch (IOException e) {
            log.warn("Новую картинку {} не удалось сохранить",  file.getOriginalFilename());
            throw new ImageAccessErrorException(file.getOriginalFilename());
        }
    }
}
