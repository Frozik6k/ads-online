package ru.skypro.homework.mapper;

import org.mapstruct.*;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.dto.comments.CommentsDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = CommentMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AdCommentsMapper {

    @Mapping(
            target = "count",
            expression = "java(ad.getComments() == null ? 0 : ad.getComments().size())"
    )
    @Mapping(
            target = "results",
            expression = "java(toCommentDtos(ad.getComments()))"
    )
    CommentsDto toCommentsDto(Ad ad);


    List<CommentDto> toCommentDtos(List<Comment> comments);
}
