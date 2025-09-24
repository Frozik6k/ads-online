package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.download.StorageFile;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdRequestDto;
import ru.skypro.homework.dto.ads.AdResponseDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.Adservice;

import java.util.List;

@RequiredArgsConstructor
public class AdServiceImpl implements Adservice {

    final private AdRepository adRepository;
    final private AdMapper adMapper;
    final private StorageFile storageFile;
    final private UserDetailsService userDetailsService;

    @Override
    public AdsDto getAllAds() {
        List<Ad>  ads = adRepository.findAll();
        return adMapper.toAdsDto(ads);
    }

    @Override
    public AdDto createAd(AdRequestDto req,  MultipartFile image) {
        Ad ad = adMapper.toEntity(req);
        ad.setImage(storageFile.download(image));
        adRepository.save(ad);
        return adMapper.toShortDto(ad);
    }

    @Override
    public AdResponseDto getAd(long id) {
        Ad ad = adRepository.getReferenceById(id);
        return adMapper.toFullDto(ad) ;
    }

    @Override
    public void deleteAd(long id) {
        adRepository.deleteById(id);
    }

    @Override
    public AdDto updateAd(long id, AdRequestDto req) {
        Ad ad = adMapper.toEntity(req);
        if (adRepository.getReferenceById(id) == null) throw new IllegalArgumentException("Объявление не найдено");
        ad.setId(id);
        adRepository.save(ad);
        return adMapper.toShortDto(ad);
    }

    @Override
    public AdsDto getCurrentUserAds() {
        //TODO необходимо получить текущего пользователя User
        List<Ad>  ads = adRepository.findAll(); //TODO сделать фильтр по user;
        return null;
    }

    @Override
    public String updateAdImage(long id, MultipartFile file) {
        Ad ad = adRepository.getReferenceById(id);
        if (ad == null) throw new IllegalArgumentException("Объявление не найдено");
        String patch = storageFile.download(file);
        ad.setImage(patch);
        adRepository.save(ad);
        return patch;
    }
}
