package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ru.skypro.homework.dto.ads.Ad;
import ru.skypro.homework.dto.ads.AdRequest;
import ru.skypro.homework.dto.ads.Ads;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
public class AdsController {
    
    @GetMapping("/ads")
    public ResponseEntity<Ads> getAllAds() {
        //TODO: implement

        return ResponseEntity.ok(
            new Ads(0, List.of(
                new Ad(1, "example/image.jpg", 1, 100, "example title")
            ))
        );
    }
    
    @PostMapping(value = "/ads", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> postMethodName(@RequestBody AdRequest req, MultipartFile image) {
        //TODO: implement
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                new Ad(1, "example/image.jpg", 1, 100, "example title")
        );
    }
    
    @GetMapping("/ads/{id}")
    public ResponseEntity<Ad> getAd(@PathVariable long id) {
        //TODO: implement

        return ResponseEntity.ok(
            new Ad(1, "example/image.jpg", 1, 100, "example title")
        );
    }
    
    @DeleteMapping("/ads/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable long id) {
        //TODO: implement

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/ads/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable long id, @RequestBody AdRequest req) {
        //TODO: implement

        return ResponseEntity.ok(
            new Ad(1, "example/image.jpg", 1, 100, "example title")
        );
    }

    @GetMapping("ads/me")
    public ResponseEntity<Ads> getCurrentUserAds(@AuthenticationPrincipal UserDetails user) {
        //TODO: implement

        // Можно передавать как параметр для сервиса
        String userName = user.getUsername();

        return ResponseEntity.ok(
            new Ads(0, List.of(
                new Ad(1, "example/image.jpg", 1, 100, "example title")
            ))
        );
    }
    
    @PatchMapping("/ads/{id}/image")
    public ResponseEntity<String> updateAdImage(@PathVariable long id, MultipartFile image) {
        //TODO: implement

        return ResponseEntity.ok(
            "example/image.jpg"
        );
    }
}
