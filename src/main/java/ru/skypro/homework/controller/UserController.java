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


    @PostMapping("/set_password")
    @Operation(summary = "Обновление пароля")
    public ResponseEntity<Void> setPassword(@AuthenticationPrincipal SecurityUser user, @RequestBody NewPasswordRequest passwordData){
        userService.setUserPassword(user.getDomainUser().getId(), passwordData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @Operation(summary = "Получить данные пользователя")
    public UserDto getUser(@AuthenticationPrincipal SecurityUser user) {

        return userService.getUser(user.getDomainUser().getId());
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновление данных пользователя")
    public UpdateUserDto updateUser(@AuthenticationPrincipal SecurityUser user, @RequestBody UpdateUserDto updateUserData) {
        return userService.updateUser(user.getDomainUser().getId(), updateUserData);
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление аватара пользователя")
    public ResponseEntity<?> updateUserImage(@AuthenticationPrincipal SecurityUser user, @RequestParam("image")MultipartFile image){
        userService.updateUserAvatar(user.getDomainUser().getId(), image);
        return ResponseEntity.ok().build();
    }

}
