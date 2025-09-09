package ru.skypro.homework.dto.user;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPasswordRequest {

    @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
    private String currentPassword;

    @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
    private String newPassword;

}
