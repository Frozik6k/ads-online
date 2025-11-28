package ru.skypro.homework.mapper;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.ads.AdResponseDto;
import ru.skypro.homework.dto.user.UpdateUserDto;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.model.User;

import java.math.BigDecimal;
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
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("password");
        user.setPhone("+79999999999");
        user.setRole(Role.USER);
        user.setImage("path/to/image.jpg");

        userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
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

    @Test
    public void givenRegister_whenUser_thenGetUser() {
        //given
        Register register = new Register();
        register.setUsername(user.getUsername());
        register.setPassword(user.getPassword());
        register.setFirstName(user.getFirstName());
        register.setLastName(user.getLastName());
        register.setRole(user.getRole());
        register.setPhone(user.getPhone());

        //when
        User userOutput = userMapper.fromRegister(register);

        //then
        assertEquals(user.getUsername(), userOutput.getUsername());
        assertEquals(user.getPassword(), userOutput.getPassword());
        assertEquals(user.getFirstName(), userOutput.getFirstName());
        assertEquals(user.getLastName(), userOutput.getLastName());
        assertEquals(user.getPhone(), userOutput.getPhone());
        assertEquals(user.getRole(), userOutput.getRole());

    }

    @Test
    public void givenAdResponseDto_whenMapping_thenGetUser() {
        //given
        AdResponseDto adResponseDto = new AdResponseDto(
                4,
                "firstName",
                "lastName",
                "description",
                "username",
                "pathAd",
                "+79999999999",
                BigDecimal.valueOf(100),
                "title"
        );

        //when
        User userOutput = userMapper.fromAdResponseDtoToUser(adResponseDto);

        //then
        assertEquals(user.getFirstName(), userOutput.getFirstName());
        assertEquals(user.getLastName(), userOutput.getLastName());
        assertEquals(user.getPhone(), userOutput.getPhone());
        assertEquals(user.getUsername(), userOutput.getUsername());
    }

    @Test
    public void givenUser_whenMapping_thenGetUpdateUserDto() {
        //given
        UpdateUserDto updateUserDto = new UpdateUserDto(
                "firstName",
                "lastName",
                "+79999999999"
        );

        //when
        UpdateUserDto updateUserDtoOutput = userMapper.toUpdateUserDto(user);

        assertEquals(updateUserDto.getFirstName(), updateUserDtoOutput.getFirstName());
        assertEquals(updateUserDto.getLastName(), updateUserDtoOutput.getLastName());
        assertEquals(updateUserDto.getPhone(), updateUserDtoOutput.getPhone());
    }

}
