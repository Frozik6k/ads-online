package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Override
    @PreAuthorize("hasRole('ADMIN') or @security.isAdOwner(#userId, authentication.name)")
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
    public UpdateUserDto updateUser(Long userId, UpdateUserDto updateUserDto) {

        User user = repository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        user.setFirstName(updateUserDto.getFirstName());
        user.setLastName(updateUserDto.getLastName());
        user.setPhone(updateUserDto.getPhone());

        User updatedUser = repository.save(user);

        return mapper.toUpdateUserDto(updatedUser);
    }

    @Override
    public void updateUserAvatar(Long userId, MultipartFile avatarFile) {

        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (user.getImage() == null) {
            user.setImage(UUID.randomUUID().toString());
        }

        imageService.updateImage(user.getImage(), avatarFile);

        repository.save(user);

    }

    @Override
    public User getByUserName(String username) {
        return repository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("Пользователь не найден")
        );
    }

}
