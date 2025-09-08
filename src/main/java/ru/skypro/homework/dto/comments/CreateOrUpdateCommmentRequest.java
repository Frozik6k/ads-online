package ru.skypro.homework.dto.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class CreateOrUpdateCommmentRequest {
    @Size(min = 8, max = 64, message = "Текст комментария")
    private String text;
}
