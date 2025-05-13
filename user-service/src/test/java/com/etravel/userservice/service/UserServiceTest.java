package com.etravel.userservice.service;

import com.etravel.userservice.domain.contracts.UserRepository;
import com.etravel.userservice.domain.dto.UserRequestDTO;
import com.etravel.userservice.domain.dto.UserResponseDTO;
import com.etravel.userservice.domain.entity.RoleType;
import com.etravel.userservice.domain.entity.User;
import com.etravel.userservice.domain.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepo;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDTO req;
    private User user;
    private UserResponseDTO resp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        req = new UserRequestDTO();
        req.setUsername("alice");
        req.setPassword("pass");
        req.setEmail("alice@example.com");
        req.setFirstName("Alice");
        req.setLastName("Smith");
        req.setRole("EMPLOYEE");

        user = new User("alice","hashed","alice@example.com","Alice","Smith", RoleType.EMPLOYEE);
        // set id via reflection or assume setter
        resp = new UserResponseDTO();
        resp.setId("1");
        resp.setUsername("alice");
    }

    @Test
    void testCreate() {
        when(userMapper.toEntity(req)).thenReturn(user);
        when(userRepo.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(resp);

        UserResponseDTO result = userService.createUser(req);
        assertEquals("1", result.getId());
        verify(userRepo).save(user);
    }

    @Test
    void testGetByIdFound() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(resp);

        UserResponseDTO result = userService.getUserById(1L);
        assertEquals("alice", result.getUsername());
    }

    @Test
    void testGetByIdNotFound() {
        when(userRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(2L));
    }

    @Test
    void testGetAll() {
        when(userRepo.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.toResponse(user)).thenReturn(resp);

        List<UserResponseDTO> list = userService.getAllUsers();
        assertEquals(1, list.size());
    }

    @Test
    void testDelete() {
        doNothing().when(userRepo).deleteById(1L);
        userService.deleteUser(1L);
        verify(userRepo).deleteById(1L);
    }
}