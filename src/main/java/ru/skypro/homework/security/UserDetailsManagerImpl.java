package ru.skypro.homework.security;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

@RequiredArgsConstructor
@Component("security")
@EnableMethodSecurity(prePostEnabled = true)
public class UserDetailsManagerImpl implements UserDetailsManager {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private org.springframework.security.core.userdetails.User userPrincipals;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow();

        userPrincipals =
            new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority(user.getRole().name()))
            );

        return userPrincipals;
    }

    @Override
    public void createUser(UserDetails user) {
        userRepository.save(userMapper.fromUserDetails(user));
    }

    @Override
    public void updateUser(UserDetails user) {
        userRepository.save(userMapper.fromUserDetails(user));
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        userRepository.delete(user);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        if (userPrincipals != null) {
            User user = userRepository.findByUsername(userPrincipals.getUsername()).orElseThrow();
            if (!user.getPassword().equals(oldPassword)) throw new IllegalArgumentException("Old password doesn't match");
            user.setPassword(newPassword);
            userRepository.save(user);
        }
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean isAdOwner(Long id, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        // верните true, если объявление с таким id принадлежит username users_id
        return adRepository.existsByIdAndUserId(id, user.getId());
    }

    public boolean isCommentOwner(Long id, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Ad ad = adRepository.findAllByUserId(user.getId()).get(0);
        return commentRepository.existsByIdAndAdId(id, ad.getId());
    }
}
