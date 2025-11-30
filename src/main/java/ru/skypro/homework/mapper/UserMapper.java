package ru.skypro.homework.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.ads.AdResponseDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.model.User;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "email", source = "username")
    })
    UserDto toUserDto(User entity);

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

    User fromRegister(Register register);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", source = "authorFirstName")
    @Mapping(target = "lastName", source = "authorLastName")
    @Mapping(target = "username", source = "email")
    @Mapping(target = "phone", source = "phone")
    User fromAdResponseDtoToUser(AdResponseDto adResponseDto);

    UpdateUserDto toUpdateUserDto(User updatedUser);
}
