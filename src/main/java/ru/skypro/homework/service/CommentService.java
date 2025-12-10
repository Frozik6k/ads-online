package ru.skypro.homework.service;

import ru.skypro.homework.dto.comments.CommentCreateOrUpdateRequest;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.model.User;

public interface CommentService {

    CommentsDto getComments(Long idAd);

    CommentDto addComment(Long idAd, CommentCreateOrUpdateRequest request, User user);

    void deleteComment(Long idAd, Long idComment);

    CommentDto updateComment(Long idAd, Long idComment, CommentCreateOrUpdateRequest request);
}
