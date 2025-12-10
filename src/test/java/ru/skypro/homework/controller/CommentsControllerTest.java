package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ru.skypro.homework.config.GlobalExceptionHandler;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.comments.CommentCreateOrUpdateRequest;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.security.SecurityUser;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @Mock
    private SecurityUser securityUser;

    @InjectMocks
    private CommentsController commentsController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentsController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.getParameterType().equals(SecurityUser.class)
                                && parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
                        return securityUser;
                    }
                })
                .build();
    }

    @Test
    void testGetComments() throws Exception {
        CommentDto commentDto = new CommentDto(
                1L, "pathAvatar", "authorFirstName",
                LocalDateTime.now(),
                10L, "Text comment"
        );

        CommentsDto commentsDto = new CommentsDto(
                1,
                List.of(commentDto)
        );

        Long idAd = 2L;

        when(commentService.getComments(idAd)).thenReturn(commentsDto);

        mockMvc.perform(get("/ads/2/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].author").value(1))
                .andExpect(jsonPath("$.results[0].authorImage").value("pathAvatar"))
                .andExpect(jsonPath("$.results[0].authorFirstName").value("authorFirstName"))
                .andExpect(jsonPath("$.results[0].pk").value(10))
                .andExpect(jsonPath("$.results[0].text").value("Text comment"));


    }

    @Test
    void testGetCommentsThenAdNotFound() throws Exception {
        when(commentService.getComments(5L)).thenThrow(new AdNotFoundException(5L));

        mockMvc.perform(get("/ads/5/comments"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddComment() throws Exception {
        CommentCreateOrUpdateRequest request = new CommentCreateOrUpdateRequest("Text comment");
        Long idAd = 2L;
        CommentDto commentDto = new CommentDto(
                1L, "pathAvatar", "authorFirstName",
                LocalDateTime.now(),
                10L, "Text comment"
        );
        User user = new User(
                2L,
                "mail@mail.ru", "11111111",
                "Вася", "Петров", "+79990022999",
                Role.USER, "path",
                null, null);

        when(commentService.addComment(idAd, request, user)).thenReturn(commentDto);
        when(securityUser.getDomainUser()).thenReturn(user);

        mockMvc.perform(post("/ads/2/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Text comment"));
    }

    @Test
    void testAddCommentThenAdNotFound() throws Exception {
        User user = new User(
                2L,
                "mail@mail.ru", "11111111",
                "Вася", "Петров", "+79990022999",
                Role.USER, "path",
                null, null);

        CommentCreateOrUpdateRequest request = new CommentCreateOrUpdateRequest("Text comment");
        when(commentService.addComment(2L, request, user)).thenThrow(new AdNotFoundException(2L));
        when(securityUser.getDomainUser()).thenReturn(user);

        mockMvc.perform(post("/ads/2/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCommentThenAdNotFound() throws Exception {
        doThrow(new AdNotFoundException(2L)).when(commentService).deleteComment(anyLong(), anyLong());
        mockMvc.perform(delete("/ads/2/comments/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteCommentThenCommentNotFound() throws Exception {
        doThrow(new CommentNotFoundException(5L)).when(commentService).deleteComment(anyLong(), anyLong());
        mockMvc.perform(delete("/ads/2/comments/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateComment() throws Exception {
        CommentCreateOrUpdateRequest request = new CommentCreateOrUpdateRequest("Text comment");
        Long idAd = 2L;
        CommentDto commentDto = new CommentDto(
                1L, "pathAvatar", "authorFirstName",
                LocalDateTime.now(),
                10L, "Text comment"
        );

        when(commentService.updateComment(idAd, commentDto.getId(), request)).thenReturn(commentDto);

        mockMvc.perform(patch("/ads/2/comments/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Text comment"));

    }

    @Test
    void testUpdateCommentThenAdNotFound() throws Exception {
        CommentCreateOrUpdateRequest request = new CommentCreateOrUpdateRequest("Text comment");
        when(commentService.updateComment(anyLong(), anyLong(), any(CommentCreateOrUpdateRequest.class))).thenThrow(new AdNotFoundException(2L));

        mockMvc.perform(patch("/ads/2/comments/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCommentThenCommentNotFound() throws Exception {
        CommentCreateOrUpdateRequest request = new CommentCreateOrUpdateRequest("Text comment");
        when(commentService.updateComment(anyLong(), anyLong(), any(CommentCreateOrUpdateRequest.class)))
                .thenThrow(new CommentNotFoundException(10L));

        mockMvc.perform(patch("/ads/2/comments/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

}
