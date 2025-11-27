package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordRequest;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.security.SecurityUser;
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
@Slf4j
public class UserController {

    private final UserService userService;


    @PostMapping("/{id}/set_password")
    @Operation(summary = "Обновление пароля")
    public ResponseEntity<Void> setPassword(@PathVariable("id") Long userId, @RequestBody NewPasswordRequest passwordData){
        userService.setUserPassword(userId, passwordData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @Operation(summary = "Получить данные пользователя")
    public UserDto getUser(@AuthenticationPrincipal SecurityUser currentUser) {

        return userService.getUser(currentUser.getDomainUser().getId());
    }

    @PatchMapping("/{id}/me")
    @Operation(summary = "Обновление данных пользователя")
    public UserDto updateUser(@PathVariable("id") Long userId, @RequestBody UpdateUserDto updateUserData) {
        return userService.updateUser(userId, updateUserData);
    }

    @PatchMapping(value = "/{id}/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление аватара пользователя")
    public ResponseEntity<?> updateUserImage(@PathVariable("id") Long userId, @RequestParam("image")MultipartFile image){
        userService.updateUserAvatar(userId, image);
        return ResponseEntity.ok().build();
    }

}
