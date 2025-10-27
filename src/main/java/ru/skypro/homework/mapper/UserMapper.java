package ru.skypro.homework.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.model.User;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User entity);

    @Mapping(target = "id", ignore = true)
    User toUserEntity(Register register);

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "password", source = "password"),
            @Mapping(target = "role", expression = "java(toRole(userDetails.getAuthorities()))")
    })
    User fromUserDetails(UserDetails userDetails);

    default Role toRole(Collection<? extends GrantedAuthority> authorities) {
        if (authorities == null) return Role.USER;
        for (GrantedAuthority authority : authorities) {
            String auth = authority.getAuthority();
            if (auth == null) continue;
            return Role.toRole(auth);
        }
        return Role.USER;
    }

}
