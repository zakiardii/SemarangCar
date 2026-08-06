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
public class ResetPasswordRequestDTO {

    @NotBlank(message = "Email wajib diisi")
    @Email(message = "Format email tidak valid")
    private String email;

    @NotBlank(message = "Nomor HP terdaftar wajib diisi")
    private String noHp;

    @NotBlank(message = "Password baru wajib diisi")
    @Size(min = 4, message = "Password minimal 4 karakter")
    private String newPassword;
}
