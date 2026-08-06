package com.rentalmobil.service;

import com.rentalmobil.dto.LoginRequestDTO;
import com.rentalmobil.dto.LoginResponseDTO;
import com.rentalmobil.dto.RegisterRequestDTO;
import com.rentalmobil.dto.UserResponseDTO;
import com.rentalmobil.entity.UserEntity;
import com.rentalmobil.exception.BadRequestException;
import com.rentalmobil.exception.ResourceNotFoundException;
import com.rentalmobil.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email sudah terdaftar dalam sistem");
        }

        UserEntity user = UserEntity.builder()
                .nama(dto.getNama())
                .email(dto.getEmail())
                .noHp(dto.getNoHp())
                .password(passwordEncoder.encode(dto.getPassword())) // Encoded via BCrypt
                .role("USER") // Paksa role menjadi USER demi keamanan
                .build();

        UserEntity savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    @Transactional
    public LoginResponseDTO login(LoginRequestDTO dto) {
        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("Email atau password salah"));

        boolean isMatch = passwordEncoder.matches(dto.getPassword(), user.getPassword()) 
                || user.getPassword().equals(dto.getPassword());

        if (!isMatch) {
            throw new BadRequestException("Email atau password salah");
        }

        // Auto-upgrade plain password to BCrypt in DB if needed
        if (!user.getPassword().startsWith("$2a$") && !user.getPassword().startsWith("$2b$")) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            userRepository.save(user);
        }

        UserResponseDTO userResponse = mapToUserResponse(user);
        return LoginResponseDTO.builder()
                .token("SESSION-" + user.getId())
                .tokenType("Bearer")
                .user(userResponse)
                .build();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan dengan ID: " + id));
        return mapToUserResponse(user);
    }

    @Transactional
    public UserResponseDTO resetPassword(com.rentalmobil.dto.ResetPasswordRequestDTO dto) {
        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email tidak terdaftar di sistem"));

        if (user.getNoHp() == null || !user.getNoHp().trim().equalsIgnoreCase(dto.getNoHp().trim())) {
            throw new BadRequestException("Nomor HP tidak cocok dengan data akun terdaftar");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        UserEntity updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    public UserResponseDTO mapToUserResponse(UserEntity user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .nama(user.getNama())
                .email(user.getEmail())
                .noHp(user.getNoHp())
                .role(user.getRole())
                .build();
    }
}
