package ru.skypro.homework.dto.ads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ad {
    private long author;

    private String image;

    private long pk;

    private int price;

    private String title;
}
