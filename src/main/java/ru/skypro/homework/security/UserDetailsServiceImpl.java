package ru.skypro.homework.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ru.skypro.homework.model.User;

public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final UserService UserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO: implement
        User user = UserService.getByUserName(username);

        org.springframework.security.core.userdetails.User userPrincipals = 
            new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );

        return userPrincipals;
    }
}
