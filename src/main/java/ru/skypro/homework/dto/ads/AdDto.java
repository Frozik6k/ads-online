package ru.skypro.homework.dto.ads;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdDto {
    @Positive(message = "Author ID должен быть положительным")
    private long author;

    @NotBlank(message = "Image не может быть пустым")
    private String image;

    @Positive(message = "PK должен быть положительным")
    private long pk;

    @PositiveOrZero
    @NotNull(message = "Цена обязательна")
    private BigDecimal price;

    @NotBlank
    @Size(min = 3, max = 100)
    private String title;
}
