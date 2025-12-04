package ru.skypro.homework.dto.ads;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Список объявлений")
@Data
@AllArgsConstructor
public class AdsDto {

    @Schema(description = "Количество объявлений")
    private int count;

    @Schema(description = "Список объявлений")
    private List<AdDto> results;
}
