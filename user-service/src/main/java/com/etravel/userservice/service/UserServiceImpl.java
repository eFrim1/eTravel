package com.etravel.userservice.service;
import com.etravel.userservice.domain.contracts.UserRepository;
import com.etravel.userservice.domain.dto.UserRequestDTO;
import com.etravel.userservice.domain.dto.UserResponseDTO;
import com.etravel.userservice.domain.entity.RoleType;
import com.etravel.userservice.domain.entity.User;
import com.etravel.userservice.domain.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepo, UserMapper userMapper) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
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
    public UserResponseDTO createUser(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        User saved = userRepo.save(user);
        return userMapper.toResponse(saved);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User existing = userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
        existing.setUsername(dto.getUsername());
        existing.setPasswordHash(userMapper.toEntity(dto).getPasswordHash());
        existing.setEmail(dto.getEmail());
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setRole(RoleType.valueOf(dto.getRole().toUpperCase()));
        User updated = userRepo.save(existing);
        return userMapper.toResponse(updated);
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}
