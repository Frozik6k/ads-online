package ru.skypro.homework.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exteded_ads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pk;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[А-Яа-яA-Za-z\\-\\s]+$", message = "Имя может содержать только буквы, пробелы и дефис")
    private String authorFirstName;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[А-Яа-яA-Za-z\\-\\s]+$", message = "Фамилия может содержать только буквы, пробелы и дефис")
    private String authorLastName;

    @NotBlank
    @Size(min = 10, max = 1000)
    private String description;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Column(
        name = "image_url"
    )
    private String image;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9\\-\\s]{7,15}$", message = "Некорректный номер телефона")
    private String phone;

    @PositiveOrZero
    @NotNull(message = "Цена обязательна")
    private BigDecimal price;

    @NotBlank
    @Size(min = 3, max = 100)
    private String title;
}
