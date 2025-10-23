package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.RestController;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.skypro.homework.service.Adservice;

@RestController
@RequestMapping("ads")
@RequiredArgsConstructor
public class AdsController {

    final private Adservice adService;

    @GetMapping("/")
    public AdsDto getAllAds() {
        return adService.getAllAds();
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AdDto createAd(@RequestBody AdRequestDto req, MultipartFile image) {
        return adService.createAd(req, image);
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
    public AdsDto getCurrentUserAds(@AuthenticationPrincipal UserDetails user) {
        String username = user.getUsername();
        return adService.getCurrentUserAds(username);
    }

    @PatchMapping("/{id}/image")
    public String updateAdImage(@PathVariable long id, MultipartFile image) {
        return adService.updateAdImage(id, image);
    }
}
