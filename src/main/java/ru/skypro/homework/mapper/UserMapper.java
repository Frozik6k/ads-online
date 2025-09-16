package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.model.User;

@Mapper
public interface UserMapper {

    UserDto toUserDto(User entity);

    @Mapping(target = "id", ignore = true)
    User toUserEntity(Register register);

}
