package ru.skypro.homework.exception;

public class AdNotFoundException extends RuntimeException {
    public AdNotFoundException(Long id) {
        super("Объявления с id " + id + " не найдено");
    }
}
