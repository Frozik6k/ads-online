package ru.skypro.homework.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPasswordRequest;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.exception.InvalidPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl service;

    @BeforeEach
    void setUo() {
        ReflectionTestUtils.setField(service, "avatarUploadPath", "test-uploads");
    }

    @Test
    void shouldUpdatePassword_whenNewDataCorrect() {

        Long userId = 1L;
        NewPasswordRequest request = new NewPasswordRequest();
        request.setCurrentPassword("0000");
        request.setNewPassword("1234");

        User user = new User();
        user.setPassword("currentPassword");
        user.setId(userId);

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(encoder.matches(request.getCurrentPassword(), user.getPassword())).thenReturn(true);
        when(encoder.matches(request.getNewPassword(), user.getPassword())).thenReturn(false);
        when(encoder.encode(request.getNewPassword())).thenReturn("newPassword");

        service.setUserPassword(userId, request);

        verify(repository).save(user);
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void shouldReturnException_whenCurrentPasswordWrong() {

        Long userId = 1L;
        NewPasswordRequest request = new NewPasswordRequest();
        request.setCurrentPassword("0000");
        request.setNewPassword("1234");

        User user = new User();
        user.setPassword("currentPassword");
        user.setId(userId);

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(encoder.matches(request.getCurrentPassword(), user.getPassword())).thenReturn(false);

        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> service.setUserPassword(userId, request));
        assertEquals("Указан некорректный пароль!", exception.getMessage());
        verify(repository, never()).save(user);
    }

    @Test
    void shouldReturnException_whenCurrentPasswordEqualNewPassword() {

        Long userId = 1L;
        NewPasswordRequest request = new NewPasswordRequest();
        request.setCurrentPassword("0000");
        request.setNewPassword("1234");

        User user = new User();
        user.setPassword("currentPassword");
        user.setId(userId);

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(encoder.matches(request.getCurrentPassword(), user.getPassword())).thenReturn(true);
        when(encoder.matches(request.getNewPassword(), user.getPassword())).thenReturn(true);

        InvalidPasswordException exception = assertThrows(InvalidPasswordException.class, () -> service.setUserPassword(userId, request));
        assertEquals("Новый пароль должен отличаться от предыдущего!", exception.getMessage());
        verify(repository, never()).save(user);
    }

    @Test
    void shouldReturnUser_whenUserIsPresent() {

        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        UserDto userDto = new UserDto();
        userDto.setId(userId);

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = service.getUser(userId);

        assertEquals(userDto, result);
        assertEquals(userId, result.getId());
    }

    @Test
    void shouldReturnException_whenUserIdIsWrong() {

        Long userId = 1L;

        when(repository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> service.getUser(userId));
        assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void shouldReturnException_whenUpdatedUserIdIsWrong() {

        Long userId = 1L;
        UpdateUserDto updateUserDto = new UpdateUserDto();

        when(repository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> service.updateUser(userId, updateUserDto));
        assertEquals("Пользователь не найден", exception.getMessage());
    }

    @Test
    void shouldReturnUpdatedUser_whenUserIsPresent() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFirstName("Name");
        user.setLastName("LastName");
        user.setPhone("79990001122");

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("Name2");
        updateUserDto.setLastName("LastName2");
        updateUserDto.setPhone("79990002233");

        User savedUser = new User();
        savedUser.setFirstName("Name2");
        savedUser.setLastName("LastName2");
        savedUser.setPhone("79990002233");

        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setFirstName("Name2");
        expectedUserDto.setLastName("LastName2");
        expectedUserDto.setPhone("79990002233");

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenReturn(savedUser);
        when(mapper.toUserDto(savedUser)).thenReturn(expectedUserDto);

        UserDto result = service.updateUser(userId, updateUserDto);

        assertEquals("Name2", result.getFirstName());
        assertEquals("LastName2", result.getLastName());
        assertEquals("79990002233", result.getPhone());

        verify(repository).save(user);
        verify(mapper).toUserDto(savedUser);
    }

    @Test
    void shouldReturnException_whenAvatarUserIdIsWrong() {

        Long userId = 1L;
        MultipartFile file = mock(MultipartFile.class);

        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.updateUserAvatar(userId, file));

    }

    @Test
    void shouldUpdateAvatar_whenUserIsPresentAndFileCorrect() throws IOException {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("avatar.jpg");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[10]));

        when(repository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenReturn(user);

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.createDirectories(any(Path.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            filesMock.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class)))
                    .thenAnswer(invocation -> null);

            service.updateUserAvatar(userId, file);

            verify(repository).findById(userId);
            verify(repository).save(user);
            assertNotNull(user.getImage());
            assertTrue(user.getImage().contains("avatar_1_"));
            assertTrue(user.getImage().contains(".jpg"));
        }
    }

    @Test
    void shouldReturnException_whenFileIsEmpty() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(true);

        when(repository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> service.updateUserAvatar(userId, file));
    }

    @Test
    void shouldThrowRuntimeException_whenIOExceptionOccurs() throws IOException {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("avatar.jpg");

        when(repository.findById(userId)).thenReturn(Optional.of(user));

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.createDirectories(any(Path.class)))
                    .thenThrow(new IOException("Ошибка создания директории"));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> service.updateUserAvatar(userId, file));

            assertEquals("Ошибка при обновлении аватара", exception.getMessage());
            assertTrue(exception.getCause() instanceof IOException);
            assertEquals("Ошибка создания директории", exception.getCause().getMessage());
        }
    }
}
