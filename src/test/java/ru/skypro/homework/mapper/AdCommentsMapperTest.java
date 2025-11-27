package ru.skypro.homework.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AdCommentsMapperTest {

    @Mock
    private CommentMapper commentMapper;

    private AdCommentsMapper adCommentsMapper;

    private CommentsDto commentsDto;
    private Ad ad;

    @BeforeEach
    public void setUp() {
        commentsDto = new CommentsDto();
        commentsDto.setCount(3);
        commentsDto.setResults(List.of(
                new CommentDto(3, "path", "User", LocalDateTime.of(2025, 5, 3, 5, 6), 4, "Comment4"),
                new CommentDto(3, "path", "User", LocalDateTime.of(2025, 5, 3, 5, 7), 5, "Comment5"),
                new CommentDto(3, "path", "User", LocalDateTime.of(2025, 5, 3, 5, 8), 6, "Comment6")
        ));
        ad = new Ad();
        ad.setId(1L);
        ad.setComments(List.of(
                new Comment(4, LocalDateTime.of(2025, 5, 3, 5, 6), "Comment4", ad),
                new Comment(5, LocalDateTime.of(2025, 5, 3, 5, 7), "Comment5", ad),
                new Comment(6, LocalDateTime.of(2025, 5, 3, 5, 8), "Comment6", ad)
        ));
        ad.setUser(new User(3, "User", "11111111", "User", "lastname", "+7999999999", Role.USER, "path", null));

        adCommentsMapper = new AdCommentsMapperImpl(commentMapper);
    }

    @Test
    public void givenAdWithComments_whenCommentsDtoWithResults_thenSuccess() {

        //given
        Comment comment = new Comment(4, LocalDateTime.of(2025, 5, 3, 5, 6), "Comment4", ad);
        CommentDto commentDto = new CommentDto(3, "path", "User", LocalDateTime.of(2025, 5, 3, 5, 6), 4, "Comment4");

        //when
        Mockito.when(commentMapper.toCommentDto(comment)).thenReturn(commentDto);

        List<CommentDto> commentsDtosOutput = adCommentsMapper.toCommentDtos(ad.getComments());


        //then
        assertEquals(commentsDtosOutput.size(), commentsDto.getResults().size());

    }

    @Test
    public void givenAd_whenCommentsDto_thenCommentsDto() {
        //when
        CommentsDto commentsDtoOutput = adCommentsMapper.toCommentsDto(ad);

        //then
        assertEquals(commentsDto.getCount(), commentsDtoOutput.getCount());
        assertEquals(commentsDto.getResults().size(), commentsDtoOutput.getResults().size());
    }
}
