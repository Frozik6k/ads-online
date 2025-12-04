package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdRequestDto;
import ru.skypro.homework.dto.ads.AdResponseDto;
import ru.skypro.homework.dto.ads.AdsDto;

public interface AdService {
    AdsDto getAllAds();

    AdDto createAd(AdRequestDto req, MultipartFile file, long userId);

    AdResponseDto getAd(long id);

    void deleteAd(long id);

    AdDto updateAd(long id, AdRequestDto req);

    AdsDto getCurrentUserAds(long id);

    void updateAdImage(long id, MultipartFile file);
}
