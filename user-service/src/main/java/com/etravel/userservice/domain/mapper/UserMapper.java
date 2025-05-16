package com.etravel.userservice.domain.mapper;

import com.etravel.userservice.domain.dto.UserRequestDTO;
import com.etravel.userservice.domain.dto.UserResponseDTO;
import com.etravel.userservice.domain.entity.RoleType;
import com.etravel.userservice.domain.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(UserRequestDTO dto) {
        return new User(
                dto.getUsername(),
                dto.getPassword(),
                dto.getEmail(),
                dto.getFirstName(),
                dto.getLastName(),
                RoleType.valueOf(dto.getRole().toUpperCase())
        );
    }

    public UserResponseDTO toResponse(User entity) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(entity.getId().toString());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setRole(entity.getRole().name());
        return dto;
    }
}