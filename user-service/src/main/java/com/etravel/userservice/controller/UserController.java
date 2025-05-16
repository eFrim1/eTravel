package com.etravel.userservice.controller;

import com.etravel.userservice.config.JwtTokenProvider;
import com.etravel.userservice.domain.dto.JwtResponseDTO;
import com.etravel.userservice.domain.dto.LoginRequestDTO;
import com.etravel.userservice.domain.dto.UserRequestDTO;
import com.etravel.userservice.domain.dto.UserResponseDTO;
import com.etravel.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService         userService;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider     tokenProvider;
    private final PasswordEncoder      pwEncoder;

    public UserController(UserService userService,
                          AuthenticationManager authManager,
                          JwtTokenProvider tokenProvider,
                          PasswordEncoder pwEncoder) {
        this.userService   = userService;
        this.authManager   = authManager;
        this.tokenProvider = tokenProvider;
        this.pwEncoder     = pwEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO dto) {
        System.out.println("Register request: " + dto);
        UserResponseDTO created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        System.out.println("Login request:"+ dto);
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(dto.getUsername(), dto.getPassword());
        Authentication auth = authManager.authenticate(authenticationRequest);
        String token = tokenProvider.generateToken(auth);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<UserResponseDTO> me(Authentication authentication) {
        String email = authentication.getName();
        UserResponseDTO dto = userService.getUserByEmail(email);
        return ResponseEntity.ok(dto);
    }
    // 3) ADMIN only
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDTO> getAll() {
        return userService.getAllUsers();
    }

    // 4) ADMIN or owner (by email)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENT') and @customUser.getEmail(principal) == #id)")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENT') and @customUser.getEmail(principal) == #id)")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable Long id,
            @RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    // 5) ADMIN only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}