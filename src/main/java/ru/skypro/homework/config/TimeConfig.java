package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class TimeConfig {
    @Bean
    public Clock clock() {
        // Системные часы в UTC — предсказуемо и тестопригодно
        return Clock.systemUTC();
    }
}
