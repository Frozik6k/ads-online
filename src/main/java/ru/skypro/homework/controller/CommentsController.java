package ru.skypro.homework.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comments.CommentCreateOrUpdateRequest;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@RequestMapping("ads")
public class CommentsController {

    @GetMapping("/{id}/comments")
    public CommentsDto getComments(@PathVariable("id") Long idAd) {
        // TODO получение данных с сервиса
        return new CommentsDto(0, new ArrayList<>());
    }

    @PostMapping("/{id}/comments")
    public CommentDto addComment(@PathVariable("id") Long idAd) {
        // TODO отправить в сервис комментарий для записи
        return new CommentDto(0,
                "путь к аватарке",
                "Имя автора",
                LocalDateTime.now(),
                0,
                "комментарий");
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("adId") Long idAd, @PathVariable("commentId") Long idComment) {
        // TODO вызвать метод сервиса для удаления комментария
        // Response:
        // 200	OK
        // 401  Unauthorized
        // 403	Forbidden
        // 404	Not found
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public CommentDto updateComment(@PathVariable("adId") Long idAd, @PathVariable("commentId") Long idComment, @RequestBody CommentCreateOrUpdateRequest request) {
        // TODO Обновить комментарий через сервис
        return new CommentDto(0,
                "путь к аватарке",
                "Имя автора",
                LocalDateTime.now(),
                0,
                "комментарий");
    }
}
