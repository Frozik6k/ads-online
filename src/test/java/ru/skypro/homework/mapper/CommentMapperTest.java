package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.comments.CommentCreateOrUpdateRequest;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CommentMapperTest {

    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    private Comment comment;
    private Ad ad;
    private CommentDto commentDto;
    private User user;

    @BeforeEach
    public void setUp() {
        ad = new Ad();
        ad.setId(1L);
        ad.setUser(new User(7, "", "", "firstName", "", "", Role.USER, "path", null, null));

        user = new User(
                1L,
                "mail@mail.ru",
                "11111111",
                "Василий",
                "Петров",
                "+79991243555",
                Role.USER,
                "path",
                null,
                null);

        comment = new Comment();
        comment.setCreatedAt(LocalDateTime.of(2025, 10, 21, 5, 50));
        comment.setText("Comment");
        comment.setAd(ad);
        comment.setId(5);
        comment.setUser(user);

        commentDto = new CommentDto();
        commentDto.setCreatedAt(LocalDateTime.of(2025, 10, 21, 5, 50));
        commentDto.setText("Comment");
        commentDto.setId(5);
        commentDto.setAuthorImage("/images/path");
        commentDto.setAuthorFirstName("Василий");
        commentDto.setIdAuthor(1L);


    }

    @Test
    public void givenAdAndCommentCreateOrUpdateRequest_whenComment_thenComment() {

        //given
        CommentCreateOrUpdateRequest commentCreateOrUpdateRequest = new CommentCreateOrUpdateRequest();
        commentCreateOrUpdateRequest.setText("Comment");

        User user = new User(
                2L,
                "mail@mail.ru", "11111111",
                "Вася", "Петров", "+79990022999",
                Role.USER, "path",
                null, null);

        //then
        Comment commentOutput = commentMapper.toComment(commentCreateOrUpdateRequest, ad, user);

        assertEquals(commentOutput.getText(), comment.getText());
        assertEquals(commentOutput.getAd(), comment.getAd());
    }

    @Test
    public void givenComment_whenCommentDto_thenComment() {

        //when
        CommentDto commentDtoOutput = commentMapper.toCommentDto(comment);

        //then
        assertEquals(commentDto.getText(), commentDtoOutput.getText());
        assertEquals(commentDto.getId(), commentDtoOutput.getId());
        assertEquals(commentDto.getAuthorImage(), commentDtoOutput.getAuthorImage());
        assertEquals(commentDto.getAuthorFirstName(), commentDtoOutput.getAuthorFirstName());
        assertEquals(commentDto.getIdAuthor(), commentDtoOutput.getIdAuthor());
        assertEquals(commentDto.getCreatedAt(), commentDtoOutput.getCreatedAt());

    }

}
