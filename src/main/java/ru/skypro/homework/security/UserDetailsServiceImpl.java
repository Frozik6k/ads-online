package ru.skypro.homework.security;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Component;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

@RequiredArgsConstructor
@Component("security")
@EnableMethodSecurity(prePostEnabled = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private org.springframework.security.core.userdetails.User userPrincipals;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));

        return new SecurityUser(user);
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
