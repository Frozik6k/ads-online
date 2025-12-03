package ru.skypro.homework.dto.ads;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "Изменение объявления")
@Data
@AllArgsConstructor
public class AdRequestDto {

    @Schema(description = "заголовок")
    private String title;

    @Schema(description = "цена товара")
    private BigDecimal price;

    @Schema(description = "описание товара")
    private String description;
}
