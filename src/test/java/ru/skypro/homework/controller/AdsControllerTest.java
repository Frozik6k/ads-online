package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.config.GlobalExceptionHandler;
import ru.skypro.homework.dto.ads.AdDto;
import ru.skypro.homework.dto.ads.AdRequestDto;
import ru.skypro.homework.dto.ads.AdResponseDto;
import ru.skypro.homework.dto.ads.AdsDto;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.security.SecurityUser;
import ru.skypro.homework.service.AdService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AdsControllerTest {

    @Mock
    private AdService adService;

    @Mock
    private SecurityUser securityUser;

    @InjectMocks
    private AdsController adsController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adsController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.getParameterType().equals(SecurityUser.class) &&
                                parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
                        return securityUser;
                    }
                })
                .build();
    }

    @Test
    void testGetAllAds() throws Exception {
        AdsDto adsDto = new AdsDto(
                1,
                List.of(
                        new AdDto(1L, "path", 2L, BigDecimal.valueOf(2), "title")
                )
        );

        when(adService.getAllAds()).thenReturn(adsDto);

        mockMvc.perform(get("/ads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adsDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].author").value(1))
                .andExpect(jsonPath("$.results[0].image").value("path"))
                .andExpect(jsonPath("$.results[0].pk").value(2))
                .andExpect(jsonPath("$.results[0].price").value(2))
                .andExpect(jsonPath("$.results[0].title").value("title"));
    }

    @Test
    void testCreateAd() throws Exception {
        AdRequestDto adRequestDto = new AdRequestDto(
                "title",
                BigDecimal.valueOf(10),
                "description"
        );

        MockMultipartFile adPart = new MockMultipartFile(
                "properties",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(adRequestDto)
        );

        MockMultipartFile imagePart = new MockMultipartFile(
                "image",
                "file.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        AdDto adDto = new AdDto(
                1L,
                "path",
                2L,
                BigDecimal.valueOf(10),
                "title"
        );

        User user = new User();
        user.setId(1L);

        when(adService.createAd(any(AdRequestDto.class), any(MultipartFile.class), anyLong()))
                .thenReturn(adDto);

        when(securityUser.getDomainUser()).thenReturn(user);

        mockMvc.perform(multipart("/ads")
                        .file(adPart)
                        .file(imagePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.author").value(1))
                .andExpect(jsonPath("$.image").value("path"))
                .andExpect(jsonPath("$.pk").value(2))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.title").value("title"));
    }

    @Test
    void testGetAd() throws Exception {
        AdResponseDto adResponseDto = new AdResponseDto(
                1L,
                "firstName",
                "lastName",
                "description",
                "user@mail.ru",
                "path",
                "+79990000000",
                BigDecimal.valueOf(10),
                "title"
        );

        when(adService.getAd(1L)).thenReturn(adResponseDto);

        mockMvc.perform(get("/ads/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorFirstName").value("firstName"))
                .andExpect(jsonPath("$.authorLastName").value("lastName"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.email").value("user@mail.ru"))
                .andExpect(jsonPath("$.image").value("path"))
                .andExpect(jsonPath("$.phone").value("+79990000000"))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.title").value("title"));
    }

    @Test
    void testGetAdThenAdNotFound() throws Exception {
        when(adService.getAd(1L)).thenThrow(new AdNotFoundException(1L));

        mockMvc.perform(get("/ads/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAdThenNoContent() throws Exception {
        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteAdThenNotFount() throws Exception {
        doThrow(new AdNotFoundException(1L)).when(adService).deleteAd(1L);

        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateAd() throws Exception {
        AdRequestDto adRequestDto = new AdRequestDto(
                "title",
                BigDecimal.valueOf(10),
                "description"
        );
        AdDto adDto = new AdDto(
                1L,
                "path",
                2L,
                BigDecimal.valueOf(10),
                "title"
        );

        when(adService.updateAd(2L, adRequestDto)).thenReturn(adDto);

        mockMvc.perform(patch("/ads/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(1))
                .andExpect(jsonPath("$.image").value("path"))
                .andExpect(jsonPath("$.pk").value(2))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.title").value("title"));
    }

    @Test
    void testUpdateAdThenAdNotFound() throws Exception {
        AdRequestDto adRequestDto = new AdRequestDto(
                "title",
                BigDecimal.valueOf(10),
                "description"
        );

        when(adService.updateAd(2L, adRequestDto)).thenThrow(new AdNotFoundException(2L));

        mockMvc.perform(patch("/ads/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adRequestDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCurrentUserAds() throws Exception {
        AdsDto adsDto = new AdsDto(
                1,
                List.of(
                        new AdDto(1L, "path", 2L, BigDecimal.valueOf(2), "title")
                )
        );

        User user = new User();
        user.setId(1L);

        when(securityUser.getDomainUser()).thenReturn(user);

        when(adService.getCurrentUserAds(1L)).thenReturn(adsDto);

        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].author").value(1))
                .andExpect(jsonPath("$.results[0].image").value("path"))
                .andExpect(jsonPath("$.results[0].pk").value(2))
                .andExpect(jsonPath("$.results[0].price").value(2))
                .andExpect(jsonPath("$.results[0].title").value("title"));
    }

    @Test
    void testUpdateAdImage() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-content".getBytes());

        mockMvc.perform(multipart("/ads/1/image")
                        .file(mockMultipartFile)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk());

        ArgumentCaptor<MultipartFile> captor = ArgumentCaptor.forClass(MultipartFile.class);
        verify(adService).updateAdImage(anyLong(), captor.capture());

        MultipartFile file = captor.getValue();

        assertThat(file.getName())
                .isEqualTo("image");
        assertThat(file.getOriginalFilename())
                .isEqualTo("test-image.jpg");
        assertThat(file.getContentType())
                .isEqualTo(MediaType.IMAGE_JPEG_VALUE);
        assertThat(file.getBytes())
                .isEqualTo("fake-image-content".getBytes());
    }

    @Test
    void testUpdateAdImageThenAdNotFound() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-content".getBytes());

        doThrow(new AdNotFoundException(1L))
                .when(adService).updateAdImage(anyLong(), any(MultipartFile.class));

        mockMvc.perform(multipart("/ads/1/image")
                        .file(mockMultipartFile)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isNotFound());
    }

}
