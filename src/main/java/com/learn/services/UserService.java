package com.learn.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.learn.entities.User;
import com.learn.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("user with email '%s' not found", email)));
    }

    // Register
    public User registerUser(User user) {
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();
        if (userExists) {
            throw new RuntimeException(
                    String.format("User with email '%s' already exists", user.getEmail()));
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

}
