package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdRequestDto;
import ru.skypro.homework.dto.ads.AdResponseDto;
import ru.skypro.homework.dto.ads.AdsDto;

@Service
public interface Adservice {
    AdsDto getAllAds();

    AdDto createAd(AdRequestDto req, MultipartFile file);

    AdResponseDto getAd(long id);

    void deleteAd(long id);

    AdDto updateAd(long id, AdRequestDto req);

    AdsDto getCurrentUserAds();

    String updateAdImage(long id, MultipartFile file);
}
