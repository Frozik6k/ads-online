package ru.skypro.homework.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long id) {
        super("Комментарий с id " + id + " не найден");
    }
}
