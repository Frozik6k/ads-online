package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordRequest;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;

public interface UserService {

    void setUserPassword(Long userId, NewPasswordRequest passwordData);

    UserDto getUser(Long userId);

    UserDto updateUser(Long userId, UpdateUserDto updateUserDto);

    void updateUserAvatar(Long userId, MultipartFile avatarFile);

}
