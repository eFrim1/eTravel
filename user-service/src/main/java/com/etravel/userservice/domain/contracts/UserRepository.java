package com.etravel.userservice.domain.contracts;

import com.etravel.userservice.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    List<User> findByRole(String role);
    User save(User user);
    void deleteById(Long id);
}