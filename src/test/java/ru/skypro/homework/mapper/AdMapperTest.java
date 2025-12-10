package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdRequestDto;
import ru.skypro.homework.dto.ads.AdResponseDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AdMapperTest {

    private AdMapper adMapper = Mappers.getMapper(AdMapper.class);

    private Ad ad;

    private AdDto adDto;

    private AdResponseDto adResponseDto;

    private AdRequestDto adRequestDto;

    @BeforeEach
    public void setup() {
        ad = new Ad(
                4,
                "description",
                "pathAd",
                BigDecimal.valueOf(100),
                "title",
                null,
                new User(5, "username", "password", "firstName", "lastName", "+79999999999", Role.USER, "pathUser", null, null)
        );
        adDto = new AdDto(
                5,
                "/images/pathAd",
                4,
                BigDecimal.valueOf(100),
                "title"
        );

        adResponseDto = new AdResponseDto(
                4,
                "firstName",
                "lastName",
                "description",
                "email",
                "/images/pathAd",
                "+79999999999",
                BigDecimal.valueOf(100),
                "title"
        );

        adRequestDto = new AdRequestDto("title", BigDecimal.valueOf(100), "description");

    }

    @Test
    public void givenAd_whenMapping_thenAdResponseDtoSuccess() {
        //when
        AdResponseDto adResponseOutput = adMapper.fromAdToAdResponseDto(ad);

        //then
        Assertions.assertEquals(adResponseOutput.getPk(), ad.getId());
        Assertions.assertEquals(adResponseOutput.getDescription(), ad.getDescription());
        Assertions.assertEquals(adResponseOutput.getTitle(), ad.getTitle());
        Assertions.assertEquals(adResponseOutput.getPrice(), ad.getPrice());
        Assertions.assertEquals(adResponseOutput.getImage(), "/images/" + ad.getImage());
        Assertions.assertEquals(adResponseOutput.getEmail(), ad.getUser().getUsername());
        Assertions.assertEquals(adResponseOutput.getPhone(), ad.getUser().getPhone());
        Assertions.assertEquals(adResponseOutput.getAuthorFirstName(), ad.getUser().getFirstName());
        Assertions.assertEquals(adResponseOutput.getAuthorLastName(), ad.getUser().getLastName());
    }

    @Test
    public void givenAd_whenMapping_thenGetAdDtoSuccess() {
        //when
        AdDto adDtoOutput = adMapper.toAdDto(ad);

        //then
        Assertions.assertEquals(adDtoOutput.getPk(), ad.getId());
        Assertions.assertEquals(adDtoOutput.getImage(), "/images/" + ad.getImage());
        Assertions.assertEquals(adDtoOutput.getTitle(), ad.getTitle());
        Assertions.assertEquals(adDtoOutput.getPrice(), ad.getPrice());
        Assertions.assertEquals(adDtoOutput.getAuthor(), ad.getUser().getId());
    }

    @Test
    public void givenAdRequestDtoAndAd_whenMapping_thenGetAdSuccess() {
        //when
        Ad adOutput = adMapper.fromAdRequestDtoToAd(ad, adRequestDto);

        //then
        Assertions.assertEquals(adRequestDto.getDescription(), adOutput.getDescription());
        Assertions.assertEquals(adRequestDto.getTitle(), adOutput.getTitle());
        Assertions.assertEquals(adRequestDto.getPrice(), adOutput.getPrice());
        Assertions.assertEquals(ad.getId(), adOutput.getId());
    }

    @Test
    public void givenAdRequestDto_whenMapping_thenGetAdSuccess() {
        //when
        Ad adOutput = adMapper.fromAdRequestDtoToAd(adRequestDto);

        //then
        Assertions.assertEquals(adRequestDto.getDescription(), adOutput.getDescription());
        Assertions.assertEquals(adRequestDto.getTitle(), adOutput.getTitle());
        Assertions.assertEquals(adRequestDto.getPrice(), adOutput.getPrice());
    }

    @Test
    public void givenAds_whenMapping_thenGetAdsDtoSuccess() {
        //given
        List<Ad> ads = List.of(ad, ad, ad, ad, ad, ad, ad, ad);

        //when
        AdsDto adsDtoOutput = adMapper.fromAdsToAdsDto(ads);

        //then
        Assertions.assertEquals(adsDtoOutput.getResults().size(), ads.size());
        Assertions.assertEquals(adsDtoOutput.getCount(), ads.size());
    }

}
