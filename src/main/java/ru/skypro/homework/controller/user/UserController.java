package ru.skypro.homework.controller.user;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<User> getUser(){

        User userData = new User();
        return ResponseEntity.ok(userData);

    }

    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(UpdateUser updateUser){

        UpdateUser updatedUser = new UpdateUser();

        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("image")MultipartFile image){

        return ResponseEntity.ok().build();

    }

}
