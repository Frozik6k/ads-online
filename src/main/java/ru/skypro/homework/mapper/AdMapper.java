package ru.skypro.homework.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdRequestDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.dto.ads.ExtendedAdDto;
import ru.skypro.homework.model.Ad;

@Mapper(componentModel = "spring")
public interface AdMapper {
    ExtendedAdDto toDto(Ad ad);

    Ad toEntity(ExtendedAdDto dto);

    AdRequestDto toRequestDto(Ad ad);

    Ad toEntity(AdRequestDto dto);

    AdDto toShortDto(Ad ad);

    Ad toEntity(AdDto dto);

    default AdsDto toAdsDto(List<Ad> ads) {
        return new AdsDto(ads.size(), ads.stream().map(this::toShortDto).toList());
    }
}
