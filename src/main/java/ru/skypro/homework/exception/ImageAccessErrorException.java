package ru.skypro.homework.exception;

public class ImageAccessErrorException extends RuntimeException {

    public ImageAccessErrorException(String UUID) {
        super("Ошибка доступа к картинке с uuid: " + UUID);
    }
}
