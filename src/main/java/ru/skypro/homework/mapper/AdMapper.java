package ru.skypro.homework.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdRequestDto;
import ru.skypro.homework.dto.ads.AdResponseDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.model.Ad;

@Mapper(componentModel = "spring")
public interface AdMapper {

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "email", source = "user.username")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorLastName", source = "user.lastName")
    AdResponseDto fromAdToAdResponseDto(Ad ad);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "user.id")
    AdDto toAdDto(Ad ad);

    Ad fromAdRequestDtoToAd(AdRequestDto adRequestDto);

    default AdsDto fromAdsToAdsDto(List<Ad> ads) {
        return new AdsDto(ads.size(), ads.stream().map(this::toAdDto).toList());
    }
}
