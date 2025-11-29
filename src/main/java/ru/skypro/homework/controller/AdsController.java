package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdRequestDto;
import ru.skypro.homework.dto.ads.AdResponseDto;
import ru.skypro.homework.dto.ads.AdsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.security.SecurityUser;
import ru.skypro.homework.service.AdService;

@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@CrossOrigin(
    value = "http://localhost:3000",
    allowCredentials = "true",
    allowedHeaders = {"Content-Type", "Authorization"},
    methods = {RequestMethod.GET, RequestMethod.POST,  RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE},
    maxAge = 3600
)
public class AdsController {

    final private AdService adService;

    @GetMapping
    public AdsDto getAllAds() {
        return adService.getAllAds();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AdDto> createAd(@RequestPart("properties") AdRequestDto req, MultipartFile image) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adService.createAd(req, image));
    }

    @GetMapping("/{id}")
    public AdResponseDto getAd(@PathVariable long id) {
        return adService.getAd(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAd(@PathVariable long id) {
        adService.deleteAd(id);
    }

    @PatchMapping("/{id}")
    public AdDto updateAd(@PathVariable long id, @RequestBody AdRequestDto req) {
        return adService.updateAd(id, req);
    }

    @GetMapping("/me")
    public AdsDto getCurrentUserAds(@AuthenticationPrincipal SecurityUser user) {
        Long id = user.getDomainUser().getId();
        return adService.getCurrentUserAds(id);
    }

    @PatchMapping("/{id}/image")
    public void updateAdImage(@PathVariable long id, MultipartFile image) {
        adService.updateAdImage(id, image);
    }
}
