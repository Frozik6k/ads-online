package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Обновление пароля пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPasswordRequest {

    @Schema(description = "Текущий пароль")
    @NotBlank
    @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
    private String currentPassword;

    @Schema(description = "Новый пароль")
    @NotBlank
    @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
    private String newPassword;

}
