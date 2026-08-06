package com.rentalmobil.service;

import com.rentalmobil.dto.LoginRequestDTO;
import com.rentalmobil.dto.LoginResponseDTO;
import com.rentalmobil.dto.RegisterRequestDTO;
import com.rentalmobil.dto.UserResponseDTO;
import com.rentalmobil.entity.UserEntity;
import com.rentalmobil.exception.BadRequestException;
import com.rentalmobil.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequestDTO registerDTO;
    private UserEntity sampleUser;

    @BeforeEach
    void setUp() {
        registerDTO = RegisterRequestDTO.builder()
                .nama("Budi Test")
                .email("budi@test.com")
                .noHp("08123456789")
                .password("password123")
                .build();

        sampleUser = UserEntity.builder()
                .id(10L)
                .nama("Budi Test")
                .email("budi@test.com")
                .noHp("08123456789")
                .password("$2a$10$encodedPassword")
                .role("USER")
                .build();
    }

    @Test
    @DisplayName("Registrasi User Baru Berhasil")
    void testRegisterSuccess() {
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn("$2a$10$encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(sampleUser);

        UserResponseDTO response = userService.register(registerDTO);

        assertNotNull(response);
        assertEquals("Budi Test", response.getNama());
        assertEquals("budi@test.com", response.getEmail());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Registrasi Gagal Karena Email Duplikat")
    void testRegisterDuplicateEmailThrowsException() {
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(true);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> userService.register(registerDTO));
        assertTrue(ex.getMessage().contains("Email sudah terdaftar"));
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Login User Berhasil")
    void testLoginSuccess() {
        LoginRequestDTO loginDTO = new LoginRequestDTO("budi@test.com", "password123");
        when(userRepository.findByEmail("budi@test.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("password123", sampleUser.getPassword())).thenReturn(true);

        LoginResponseDTO response = userService.login(loginDTO);

        assertNotNull(response);
        assertEquals("SESSION-10", response.getToken());
        assertEquals("budi@test.com", response.getUser().getEmail());
    }
}
