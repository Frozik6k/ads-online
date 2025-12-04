package ru.skypro.homework.dto.comments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Количество комментариев")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsDto {

    @Schema(description = "Количество комментариев")
    private int count;

    @Schema(description = "Комментарии")
    private List<CommentDto> results;

}
