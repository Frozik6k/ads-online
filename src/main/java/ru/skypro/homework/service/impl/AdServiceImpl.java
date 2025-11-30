package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.download.StorageFile;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdRequestDto;
import ru.skypro.homework.dto.ads.AdResponseDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdServiceImpl implements AdService {

    final private AdRepository adRepository;
    final private AdMapper adMapper;
    final private StorageFile storageFile;
    private final UserRepository userRepository;

    @Override
    @PreAuthorize("permitAll()")
    public AdsDto getAllAds() {
        List<Ad> ads = adRepository.findAll();
        return adMapper.fromAdsToAdsDto(ads);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public AdDto createAd(AdRequestDto req, MultipartFile image, long userId) {
        Ad ad = adMapper.fromAdRequestDtoToAd(req);
        ad.setImage(storageFile.download(image));

        User user = new User();
        user.setId(userId);
        ad.setUser(user);
        adRepository.save(ad);
        return adMapper.toAdDto(ad);
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public AdResponseDto getAd(long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException(id));
        return adMapper.fromAdToAdResponseDto(ad);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @security.isAdOwner(#id, authentication.name)")
    public void deleteAd(long id) {
        adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException(id));
        adRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @security.isAdOwner(#id, authentication.name)")
    public AdDto updateAd(long id, AdRequestDto req) {
        Ad ad = adMapper.fromAdRequestDtoToAd(req);
        adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException(id));
        ad.setId(id);
        adRepository.save(ad);
        return adMapper.toAdDto(ad);
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public AdsDto getCurrentUserAds(long id) {
        return adMapper.fromAdsToAdsDto(adRepository.findAllByUserId(id));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @security.isAdOwner(#id, authentication.name)")
    public void updateAdImage(long id, MultipartFile file) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException(id));
        String patch = storageFile.download(file);
        ad.setImage(patch);
        adRepository.save(ad);
    }
}
