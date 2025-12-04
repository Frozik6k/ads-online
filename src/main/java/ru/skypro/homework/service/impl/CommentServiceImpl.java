package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.comments.CommentCreateOrUpdateRequest;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.mapper.AdCommentsMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;
    private final AdCommentsMapper adCommentsMapper;

    @Override
    @PreAuthorize("isAuthenticated()")
    public CommentsDto getComments(Long idAd) {
        Ad ad = adRepository.findById(idAd)
                .orElseThrow(() -> new AdNotFoundException(idAd));
        return adCommentsMapper.toCommentsDto(ad);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public CommentDto addComment(Long idAd, CommentCreateOrUpdateRequest request) {
        Ad ad = adRepository.findById(idAd)
                .orElseThrow(() -> new AdNotFoundException(idAd));
        Comment comment = commentMapper.toComment(request, ad);
        commentRepository.save(comment);
        return commentMapper.toCommentDto(comment);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @security.isCommentOwner(#idComment, authentication.name)")
    public void deleteComment(Long idAd, Long idComment) {
        adRepository.findById(idAd)
                .orElseThrow(() -> new AdNotFoundException(idAd));
        Comment comment = commentRepository.findById(idComment)
                .orElseThrow(() -> new CommentNotFoundException(idComment));
        commentRepository.delete(comment);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @security.isCommentOwner(#idComment, authentication.name)")
    public CommentDto updateComment(Long idAd, Long idComment, CommentCreateOrUpdateRequest request) {
        adRepository.findById(idAd)
                .orElseThrow(() -> new AdNotFoundException(idAd));
        Comment comment = commentRepository.findById(idComment)
                .orElseThrow(() -> new CommentNotFoundException(idComment));

        comment.setText(request.getText());
        commentRepository.save(comment);

        return commentMapper.toCommentDto(comment);
    }
}
