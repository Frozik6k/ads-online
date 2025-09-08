package ru.skypro.homework.dto.comments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentDto {
    @JsonProperty("author")
    private long idAuthor;
    private String authorImage;
    private String authorFirstName;
    private LocalDateTime createAt;
    @JsonProperty("pk")
    private long idComment;
    private String text;
}
