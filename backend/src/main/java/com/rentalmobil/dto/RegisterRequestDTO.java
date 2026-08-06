package com.rentalmobil.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {

    @NotBlank(message = "Nama wajib diisi")
    private String nama;

    @NotBlank(message = "Email wajib diisi")
    @Email(message = "Format email tidak valid")
    private String email;

    @NotBlank(message = "No HP wajib diisi")
    @jakarta.validation.constraints.Pattern(regexp = "^(08|\\+?62)[0-9]{8,13}$", message = "Format nomor HP harus angka valid (contoh: 08123456789)")
    private String noHp;

    @NotBlank(message = "Password wajib diisi")
    @Size(min = 6, message = "Password minimal 6 karakter")
    private String password;
}
