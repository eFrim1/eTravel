package com.etravel.userservice.service;

import com.etravel.userservice.domain.dto.UserRequestDTO;
import com.etravel.userservice.domain.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO createUser(UserRequestDTO dto);
    UserResponseDTO updateUser(Long id, UserRequestDTO dto);
    void deleteUser(Long id);

}