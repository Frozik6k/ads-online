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
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.user.NewPasswordRequest;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.exception.InvalidPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.User;

import ru.skypro.homework.security.SecurityUser;
import ru.skypro.homework.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;


    @Mock
    private SecurityUser securityUser;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.getParameterType().equals(SecurityUser.class)
                                && parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter,
                                                  ModelAndViewContainer mavContainer,
                                                  NativeWebRequest webRequest,
                                                  WebDataBinderFactory binderFactory) {
                        return securityUser; // наш мок
                    }
                })
                .build();

    }

    @Test
    void testGetUser() throws Exception {

        User user = new User(1L, "user@mail.ru", "password",
                "Ivan", "Ivanov", "+79990000000", Role.USER,
                "path", null);

        UserDto userDto = new UserDto(1L, "user@mail.ru",
                "Ivan", "Ivanov", "+79990000000", Role.USER,
                "path");

        when(securityUser.getDomainUser())
                .thenReturn(user);
        when(userService.getUser(1L))
                .thenReturn(userDto);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@mail.ru"))
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.lastName").value("Ivanov"))
                .andExpect(jsonPath("$.phone").value("+79990000000"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.image").value("path"));

    }

    @Test
    void testSetPassword() throws Exception {

        User user = new User(1L, "user@mail.ru", "password",
                "Ivan", "Ivanov", "+79990000000", Role.USER,
                "path", null);
        NewPasswordRequest request = new NewPasswordRequest("password", "password");

        when(securityUser.getDomainUser())
                .thenReturn(user);

        doThrow(new UserNotFoundException("Пользователь не найден"))
                .when(userService)
                .setUserPassword(1L, request);
        mockMvc.perform(post("/users/set_password")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        doThrow(new InvalidPasswordException("Указан некорректный пароль!"))
                .when(userService)
                .setUserPassword(1L, request);
        mockMvc.perform(post("/users/set_password")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    void testUpdateUser() throws Exception {
        User user = new User(1L, "user@mail.ru", "password",
                "Ivan", "Ivanov", "+79990000000", Role.USER,
                "path", null);
        UpdateUserDto request = new UpdateUserDto("Ivan", "Ivanov", "+79990000000");


        when(securityUser.getDomainUser())
                .thenReturn(user);
        when(userService.updateUser(1L, request)).thenReturn(request);

        mockMvc.perform(patch("/users/me")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.lastName").value("Ivanov"))
                .andExpect(jsonPath("$.phone").value("+79990000000"));

    }

    @Test
    void testUpdateUserImage() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-content".getBytes());
        User user = new User(1L, "user@mail.ru", "password",
                "Ivan", "Ivanov", "+79990000000", Role.USER,
                "path", null);

        when(securityUser.getDomainUser()).thenReturn(user);

        mockMvc.perform(multipart("/users/me/image")
                        .file(image)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk());

        // Захватываем MultipartFile, с которым контроллер вызвал сервис
        ArgumentCaptor<MultipartFile> captor = ArgumentCaptor.forClass(MultipartFile.class);
        verify(userService).updateUserAvatar(any(), captor.capture());

        MultipartFile passedFile = captor.getValue();

        assertThat(passedFile.getName()).isEqualTo("image");
        assertThat(passedFile.getOriginalFilename()).isEqualTo("test-image.jpg");
        assertThat(passedFile.getContentType()).isEqualTo(MediaType.IMAGE_JPEG_VALUE);
        assertThat(passedFile.getBytes()).isEqualTo("fake-image-content".getBytes());
    }

}
