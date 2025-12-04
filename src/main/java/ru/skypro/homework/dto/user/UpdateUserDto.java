package ru.skypro.homework.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Обновление пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    @Schema(description = "Имя пользователя")
    @Size(min = 3, max = 10, message = "Имя должно иметь от 3 до 10 символов")
    private String firstName;

    @Schema(description = "Фамилия пользователя")
    @Size(min = 3, max = 10, message = "Фамилия должна содержать от 3 до 10 символов")
    private String lastName;

    @Schema(description = "Телефонный номер пользователя")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", message = "Телефон должен соответствовать формату +7 (ххх) ххх-хх-хх")
    private String phone;

}
