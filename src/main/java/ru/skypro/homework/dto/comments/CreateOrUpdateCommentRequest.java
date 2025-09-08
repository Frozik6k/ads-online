package ru.skypro.homework.dto.comments;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateCommentRequest {
    @Size(min = 8, max = 64, message = "Текст комментария")
    private String text;
}
