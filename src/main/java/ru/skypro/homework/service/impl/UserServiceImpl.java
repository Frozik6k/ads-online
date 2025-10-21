package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordRequest;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.exception.InvalidPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${avatar.upload.path}")
    private String avatarUploadPath;

    @Override
    public void setUserPassword(Long userId, NewPasswordRequest passwordData) {
        User user = repository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        //Проверяем соответствие старого пароля существующему
        if (!passwordEncoder.matches(passwordData.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Указан некорректный пароль!");
        }

        //Проверяем что новый пароль не совпадает с настоящим
        if (passwordEncoder.matches(passwordData.getNewPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Новый пароль должен отличаться от предыдущего!");
        }

        user.setPassword(passwordEncoder.encode(passwordData.getNewPassword()));

        repository.save(user);
    }

    @Override
    public UserDto getUser(Long userId) {
        User user = repository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        return mapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UpdateUserDto updateUserDto) {

        User user = repository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        user.setFirstName(updateUserDto.getFirstName());
        user.setLastName(updateUserDto.getLastName());
        user.setPhone(updateUserDto.getPhone());

        User updatedUser = repository.save(user);

        return mapper.toUserDto(updatedUser);
    }

    @Override
    public void updateUserAvatar(Long userId, MultipartFile avatarFile) {

        User user = repository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (avatarFile.isEmpty()) {
            throw new IllegalArgumentException("Аватар не может быть пустым!");
        }

        //Генерация имени файла
        String fileName = avatarFile.getOriginalFilename();
        String fileExtension = fileName != null ? fileName.substring(fileName.lastIndexOf(".")) : ".jpg";
        String newFineName = "avatar_" + userId + "_" + System.currentTimeMillis() + fileExtension;

        Path filePath = Path.of(avatarUploadPath, newFineName);

        try {
            Files.createDirectories(filePath.getParent());

            try (InputStream inputStream = avatarFile.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            user.setImage(filePath.toString());
            repository.save(user);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при обновлении аватара", e);
        }

    }

    @Override
    public User getByUserName(String username) {
        return repository.findByUsername(username).orElseThrow(
            () -> new UserNotFoundException("Пользователь не найден")
        );
    }

}
