package ru.skypro.homework.mapper;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {


    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private User user;
    private UserDto userDto;


    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("Email");
        user.setPassword("Password");
        user.setPhone("+79082691460");
        user.setRole(Role.USER);
        user.setImage("path/to/image.jpg");

        userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setRole(user.getRole());
        userDto.setImage(user.getImage());
    }

    @Test
    public void givenUser_whenGetUserDto_thenGetUserDto() {
        //then
        assertEquals(userDto,  userMapper.toUserDto(user));
    }

    @Test
    public void givenUserDetail_whenGetUser_thenGetUserWithUserNamePasswordRole() {
        //given
        UserDetails userDetail = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );

        //when
        User userOutput = userMapper.fromUserDetails(userDetail);

        //then
        assertEquals(user.getUsername(), userOutput.getUsername());
        assertEquals(user.getPassword(), userOutput.getPassword());
        assertEquals(user.getRole(), userOutput.getRole());

    }

    @Test
    public void givenGrantedAuthorityWithNull_whenGetRoleUser_thenGetRoleWithUser() {
        //given
        Collection<GrantedAuthority> grantedAuthorities = null;

        //when
        Role role = Role.USER;

        //then
        assertEquals(role, userMapper.toRole(grantedAuthorities));

    }

    @Test
    public void givenGrantedAuthorityWithUser_whenGetRoleUser_thenGetRoleWithUser() {
        //given
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        Collection<GrantedAuthority> grantedAuthorities = List.of(grantedAuthority);

        //when
        Role role = Role.USER;

        //then
        assertEquals(role, userMapper.toRole(grantedAuthorities));

    }

    @Test
    public void givenGrantedAuthorityWithAdmin_whenGetRoleAdmin_thenGetRoleWithAdmin() {
        //given
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
        Collection<GrantedAuthority> grantedAuthorities = List.of(grantedAuthority);

        //when
        Role role = Role.ADMIN;

        //then
        assertEquals(role, userMapper.toRole(grantedAuthorities));

    }

}
