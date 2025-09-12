package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.comments.CommentCreateOrUpdateRequest;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final
    private final CommentMapper commentMapper;

    @Override
    public CommentsDto getComments(Long idAd) {

        return null;
    }

    @Override
    public CommentDto addComment(Long idAd) {
        return null;
    }

    @Override
    public void deleteComment() {

    }

    @Override
    public CommentDto updateComment(Long idAd, Long idComment, CommentCreateOrUpdateRequest request) {
        return null;
    }
}
