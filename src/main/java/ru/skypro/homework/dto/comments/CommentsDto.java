package ru.skypro.homework.dto.comments;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CommentsDto {
    private int count;
    private List<CommentDto> results;
}
