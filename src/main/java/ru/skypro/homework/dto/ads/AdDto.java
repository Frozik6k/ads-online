package ru.skypro.homework.dto.ads;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Объявление")
@Data
@AllArgsConstructor
public class AdDto {

    @Schema(description = "id автора")
    private long author;

    @Schema(description = "картинка товара")
    private String image;

    @Schema(description = "id объявления")
    private long pk;

    @Schema(description = "цена товара")
    private BigDecimal price;

    @Schema(description = "заголовок объявления")
    private String title;
}
