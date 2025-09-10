package ru.skypro.homework.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.comments.CommentCreateOrUpdateRequest;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@RequestMapping("ads")
@Tag(name = "Комментарии")
public class CommentsController {

    @GetMapping("/{id}/comments")
    @Operation(summary = "Получить комментарии объявления")
    public CommentsDto getComments(@PathVariable("id") Long idAd) {
        // TODO получение данных с сервиса
        return new CommentsDto(0, new ArrayList<>());
    }

    @PostMapping("/{id}/comments")
    @Operation(summary = "Добавить комментарий к объявлению")
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
    @Operation(summary = "Удалить комментарий из объявления")
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
    @Operation(summary = "Обновить текст комментария")
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
