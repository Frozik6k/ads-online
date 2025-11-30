package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    @Override
    public boolean login(String userName, String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            if (!userDetails.getUsername().equals(userName)) {
                return false;
            }
            if (encoder.matches(password, userDetails.getPassword())) {
                return true;
            }
        } catch (UsernameNotFoundException e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean register(Register register) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(register.getUsername());
        } catch (UsernameNotFoundException e) {
            register.setPassword(encoder.encode(register.getPassword()));
            userRepository.save(userMapper.fromRegister(register));
            return true;
        }
        return false;
    }

}
