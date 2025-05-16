package com.etravel.userservice.service;
import com.etravel.userservice.domain.contracts.UserRepository;
import com.etravel.userservice.domain.dto.UserRequestDTO;
import com.etravel.userservice.domain.dto.UserResponseDTO;
import com.etravel.userservice.domain.entity.RoleType;
import com.etravel.userservice.domain.entity.User;
import com.etravel.userservice.domain.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepo, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found: " + email));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        // **Hash the raw password before saving**
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        User saved = userRepo.save(user);
        return userMapper.toResponse(saved);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User existing = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        existing.setEmail(dto.getEmail());
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setUsername(dto.getUsername());
        existing.setRole(RoleType.valueOf(dto.getRole().toUpperCase()));

        // **If the front-end sent a new password, hash that too**
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existing.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        User updated = userRepo.save(existing);
        return userMapper.toResponse(updated);
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }


}
