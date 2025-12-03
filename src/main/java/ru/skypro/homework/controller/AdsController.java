package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
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
import ru.skypro.homework.security.SecurityUser;
import ru.skypro.homework.service.AdService;

@Slf4j
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
public class AdsController {

    final private AdService adService;

    @Operation(summary = "Получение всех объявлений")
    @GetMapping
    public AdsDto getAllAds() {
        return adService.getAllAds();
    }

    @Operation(summary = "Создание нового объявления")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AdDto> createAd(
            @AuthenticationPrincipal SecurityUser securityUser,
            @RequestPart("properties") AdRequestDto req,
            MultipartFile image) {

        long userId = securityUser.getDomainUser().getId();

        return ResponseEntity.status(HttpStatus.CREATED).body(adService.createAd(req, image, userId));
    }

    @Operation(summary = "Получения объявления по id")
    @GetMapping("/{id}")
    public AdResponseDto getAd(@PathVariable long id) {
        return adService.getAd(id);
    }

    @Operation(summary = "Удаление объявления с id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAd(@PathVariable long id) {
        adService.deleteAd(id);
    }

    @Operation(summary = "Обновление данных объявления с id")
    @PatchMapping("/{id}")
    public AdDto updateAd(@PathVariable long id, @RequestBody AdRequestDto req) {
        return adService.updateAd(id, req);
    }

    @Operation(summary = "Получение объявлений текущего пользователя")
    @GetMapping("/me")
    public AdsDto getCurrentUserAds(@AuthenticationPrincipal SecurityUser user) {
        long id = user.getDomainUser().getId();
        return adService.getCurrentUserAds(id);
    }

    @Operation(summary = "Обновление картинки объявления")
    @PatchMapping("/{id}/image")
    public void updateAdImage(@PathVariable long id, MultipartFile image) {
        adService.updateAdImage(id, image);
    }
}
