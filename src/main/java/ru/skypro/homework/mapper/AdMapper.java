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
    @Mapping(target = "image", expression = "java(\"/images/\" + ad.getImage())")
    AdResponseDto fromAdToAdResponseDto(Ad ad);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "image", expression = "java(\"/images/\" + ad.getImage())")
    AdDto toAdDto(Ad ad);

    @Mapping(target = "title", source = "adRequestDto.title")
    @Mapping(target = "price", source = "adRequestDto.price")
    @Mapping(target = "description", source = "adRequestDto.description")
    Ad fromAdRequestDtoToAd(Ad ad, AdRequestDto adRequestDto);

    Ad fromAdRequestDtoToAd(AdRequestDto adRequestDto);

    default AdsDto fromAdsToAdsDto(List<Ad> ads) {
        return new AdsDto(ads.size(), ads.stream().map(this::toAdDto).toList());
    }
}
