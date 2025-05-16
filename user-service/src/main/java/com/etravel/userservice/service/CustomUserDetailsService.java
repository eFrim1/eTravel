package com.etravel.userservice.service;

import com.etravel.userservice.domain.contracts.UserRepository;
import com.etravel.userservice.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User.UserBuilder;

@Service

public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        System.out.println(userRepo.findAll());
        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                {
                    System.out.println("here");
                    return new UsernameNotFoundException("No user with email " + email);
                });
        UserBuilder builder = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .roles(user.getRole().name());
        return builder.build();
    }
}