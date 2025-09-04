package ru.skypro.homework.dto.user;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UpdateUser {

    @Size(min = 3, max = 10, message = "Имя должно иметь от 3 до 10 символов")
    private String firstName;

    @Size(min = 3, max = 10, message = "Фамилия должна содержать от 3 до 10 символов")
    private String lastName;

    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", message = "Телефон должен соответствовать формату +7 (ххх) ххх-хх-хх")
    private String phone;

    public UpdateUser(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public UpdateUser(){}

    public @Size(min = 3, max = 10, message = "Имя должно иметь от 3 до 10 символов") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Size(min = 3, max = 10, message = "Имя должно иметь от 3 до 10 символов") String firstName) {
        this.firstName = firstName;
    }

    public @Size(min = 3, max = 10, message = "Фамилия должна содержать от 3 до 10 символов") String getLastName() {
        return lastName;
    }

    public void setLastName(@Size(min = 3, max = 10, message = "Фамилия должна содержать от 3 до 10 символов") String lastName) {
        this.lastName = lastName;
    }

    public @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", message = "Телефон должен соответствовать формату +7 (ххх) ххх-хх-хх") String getPhone() {
        return phone;
    }

    public void setPhone(@Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}", message = "Телефон должен соответствовать формату +7 (ххх) ххх-хх-хх") String phone) {
        this.phone = phone;
    }
}
