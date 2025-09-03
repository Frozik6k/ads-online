package ru.skypro.homework.dto.ads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdRequest {
    private String title;
    
    private int price;

    private String description;
}
