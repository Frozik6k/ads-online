package ru.skypro.homework.security;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionResponse {

    private int status;

    private String message;

    private String details;

    private String url;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime timestamp;

    public ExceptionResponse(HttpStatus status, String message, String details, String url, Clock clock) {
        this.status = status.value();
        this.message = message;
        this.details = details;
        this.url = url;
        this.timestamp = OffsetDateTime.now(clock).withOffsetSameInstant(ZoneOffset.UTC);
    }

    public static ExceptionResponse of(HttpStatus status, String message, String details, String url, Clock clock) {
        return new ExceptionResponse(status, message, details, url, clock);
    }
}
