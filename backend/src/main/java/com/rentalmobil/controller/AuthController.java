package com.rentalmobil.controller;

import com.rentalmobil.dto.LoginRequestDTO;
import com.rentalmobil.dto.LoginResponseDTO;
import com.rentalmobil.dto.RegisterRequestDTO;
import com.rentalmobil.dto.ResetPasswordRequestDTO;
import com.rentalmobil.dto.UserResponseDTO;
import com.rentalmobil.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        UserResponseDTO response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<UserResponseDTO> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
        UserResponseDTO response = userService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}
