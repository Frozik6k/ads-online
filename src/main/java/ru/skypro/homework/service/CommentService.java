package ru.skypro.homework.service;

import ru.skypro.homework.dto.comments.CommentCreateOrUpdateRequest;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;

public interface CommentService {

    CommentsDto getComments(Long idAd);

    CommentDto addComment(Long idAd);

    void deleteComment();

    CommentDto updateComment(Long idAd, Long idComment, CommentCreateOrUpdateRequest request);
}
