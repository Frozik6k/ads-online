package ru.skypro.homework.security;

import java.io.IOException;


import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        
        ExceptionResponse errorResponse = new ExceptionResponse(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized",
                authException.getMessage(),
                request.getServletPath()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        mapper.writeValue(response.getWriter(), errorResponse);
    }
}
