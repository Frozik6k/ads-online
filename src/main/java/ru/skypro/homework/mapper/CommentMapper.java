package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.comments.CommentCreateOrUpdateRequest;
import ru.skypro.homework.dto.comments.CommentDto;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "idAuthor", expression = "java(entity.getUser().getId())")
    @Mapping(target = "authorImage", expression = "java(\"/images/\" + entity.getUser().getImage())")
    @Mapping(target = "authorFirstName", expression = "java(entity.getUser().getFirstName())")
    CommentDto toCommentDto(Comment entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "ad", expression = "java(ad)")
    @Mapping(target = "user", expression = "java(user)")
    Comment toComment(CommentCreateOrUpdateRequest request, Ad ad, User user);

}
