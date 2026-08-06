package com.rentalmobil.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDTO {

    @NotNull(message = "User ID wajib diisi")
    private Long userId;

    @NotNull(message = "Mobil ID wajib diisi")
    private Long mobilId;

    @NotNull(message = "Tanggal mulai wajib diisi")
    @FutureOrPresent(message = "Tanggal harus hari ini atau yang akan datang")
    private LocalDate tanggalMulai;

    @Min(value = 1, message = "Durasi minimal 1 hari")
    private Integer durasiHari;

    @NotNull(message = "Status dengan sopir wajib diisi")
    private Boolean denganSopir;

    private String catatan;

    private String metodePembayaran;

    private String buktiPembayaranUrl;
}
