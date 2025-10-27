package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordRequest;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.service.UserService;

@CrossOrigin(
        value = "http://localhost:3000",
        allowCredentials = "true",
        allowedHeaders = {"Content-Type", "Authorization"},
        methods = {RequestMethod.GET, RequestMethod.POST,  RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE},
        maxAge = 3600
)
@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/{id}/set_password")
    @Operation(summary = "Обновление пароля")
    public ResponseEntity<Void> setPassword(@PathVariable("id") Long userId, @RequestBody NewPasswordRequest passwordData){
        service.setUserPassword(userId, passwordData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/me")
    @Operation(summary = "Получить данные пользователя")
    public UserDto getUser(@PathVariable("id") Long userId) {
        return service.getUser(userId);
    }

    @PatchMapping("/{id}/me")
    @Operation(summary = "Обновление данных пользователя")
    public UserDto updateUser(@PathVariable("id") Long userId, @RequestBody UpdateUserDto updateUserData) {
        return service.updateUser(userId, updateUserData);
    }

    @PatchMapping(value = "/{id}/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление аватара пользователя")
    public ResponseEntity<?> updateUserImage(@PathVariable("id") Long userId, @RequestParam("image")MultipartFile image){
        service.updateUserAvatar(userId, image);
        return ResponseEntity.ok().build();
    }

}
