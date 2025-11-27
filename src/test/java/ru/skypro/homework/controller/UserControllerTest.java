package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.user.NewPasswordRequest;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.exception.InvalidPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.security.SecurityUser;
import ru.skypro.homework.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        mockMvc.perform(get("/users/me")
                        .principal(() -> "user@mail.ru"))
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

        NewPasswordRequest request = new NewPasswordRequest("password", "password");

        doThrow(UserNotFoundException.class)
                .when(userService)
                .setUserPassword(1L, request);

        mockMvc.perform(post("/users/1/set_password")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());


    }
}
