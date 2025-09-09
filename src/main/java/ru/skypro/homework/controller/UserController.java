package ru.skypro.homework.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.User;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody NewPassword passwordData){

        return ResponseEntity.ok().build();

    }

    @GetMapping("/me")
    public UserDto getUser() {
        return new UserDto();
    }

    @PatchMapping("/me")
    public UpdateUserDto updateUser(@RequestBody UpdateUserDto updateUser) {
        return new UpdateUserDto();
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("image")MultipartFile image){

        return ResponseEntity.ok().build();

    }

}
