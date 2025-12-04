package ru.skypro.homework.dto.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Комментарий к объявлению")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @Schema(description = "Идентификатор автора")
    @JsonProperty("author")
    private long idAuthor;

    @Schema(description = "Ссылка на изображение автора")
    private String authorImage;

    @Schema(description = "Имя автора")
    private String authorFirstName;

    @Schema(description = "Дата создания")
    private LocalDateTime createdAt;

    @Schema(description = "Идентификатор комментария")
    @JsonProperty("pk")
    private long id;

    @Schema(description = "Текст комментария")
    private String text;
}
