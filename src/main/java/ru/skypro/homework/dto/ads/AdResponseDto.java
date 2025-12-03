package ru.skypro.homework.dto.ads;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Объявление")
@Data
@AllArgsConstructor
public class AdResponseDto {

    @Schema(description = "Индификатор объявления")
    private long pk;

    @Schema(description = "Имя автора")
    private String authorFirstName;

    @Schema(description = "Фамилия автора")
    private String authorLastName;

    @Schema(description = "Описание товара")
    private String description;

    @Schema(description = "Email автора")
    private String email;

    @Schema(description = "путь к картинке товара")
    private String image;

    @Schema(description = "Номер телефона автора")
    private String phone;

    @Schema(description = "Цена товара")
    private BigDecimal price;

    @Schema(description = "Название товара")
    private String title;
}
