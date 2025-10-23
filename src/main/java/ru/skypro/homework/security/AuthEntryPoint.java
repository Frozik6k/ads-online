package ru.skypro.homework.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@AllArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    private final Clock clock;

    @Override
    public void commence(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException {

        ExceptionResponse errorResponse = new ExceptionResponse(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized",
                authException.getMessage(),
                request.getServletPath(), clock
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        mapper.writeValue(response.getWriter(), errorResponse);
    }
}
