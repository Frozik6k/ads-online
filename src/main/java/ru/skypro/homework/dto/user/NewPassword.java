package ru.skypro.homework.dto.user;

import javax.validation.constraints.Size;

public class NewPassword {

    @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
    private String currentPassword;

    @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
    private String newPassword;

    public NewPassword(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов") String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(@Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов") String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов") String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов") String newPassword) {
        this.newPassword = newPassword;
    }
}
