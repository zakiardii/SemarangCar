package com.rentalmobil.service;

import com.rentalmobil.dto.BookingRequestDTO;
import com.rentalmobil.entity.BookingEntity;
import com.rentalmobil.entity.MobilEntity;
import com.rentalmobil.entity.UserEntity;
import com.rentalmobil.exception.BadRequestException;
import com.rentalmobil.repository.BookingRepository;
import com.rentalmobil.repository.MobilRepository;
import com.rentalmobil.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MobilRepository mobilRepository;

    @InjectMocks
    private BookingService bookingService;

    private UserEntity sampleUser;
    private MobilEntity sampleMobil;
    private BookingRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        sampleUser = UserEntity.builder()
                .id(1L)
                .nama("Pelanggan Test")
                .email("user@test.com")
                .role("USER")
                .build();

        sampleMobil = MobilEntity.builder()
                .id(5L)
                .nama("Avanza Test")
                .harga(300000.0)
                .statusTersedia(true)
                .isDeleted(false)
                .build();

        requestDTO = BookingRequestDTO.builder()
                .userId(1L)
                .mobilId(5L)
                .tanggalMulai(LocalDate.now().plusDays(1))
                .durasiHari(3)
                .denganSopir(true)
                .catatan("Sewa untuk tes")
                .build();
    }

    @Test
    @DisplayName("Pemesanan Mobil Berhasil Dibuat")
    void testCreateBookingSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(mobilRepository.findById(5L)).thenReturn(Optional.of(sampleMobil));
        when(bookingRepository.findActiveBookingsByMobilId(5L)).thenReturn(Collections.emptyList());

        BookingEntity mockSavedBooking = BookingEntity.builder()
                .id(100L)
                .user(sampleUser)
                .mobil(sampleMobil)
                .tanggalMulai(requestDTO.getTanggalMulai())
                .durasiHari(3)
                .denganSopir(true)
                .totalHarga(1350000.0) // (300.000 + 150.000) * 3 = 1.350.000
                .status("PENDING")
                .build();

        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(mockSavedBooking);

        BookingEntity result = bookingService.createBooking(requestDTO);

        assertNotNull(result);
        assertEquals(1350000.0, result.getTotalHarga());
        assertEquals("PENDING", result.getStatus());
        verify(bookingRepository, times(1)).save(any(BookingEntity.class));
    }

    @Test
    @DisplayName("Pemesanan Gagal Karena Bentrok Tanggal (Double Booking)")
    void testCreateBookingDoubleBookingThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(mobilRepository.findById(5L)).thenReturn(Optional.of(sampleMobil));

        BookingEntity existingBooking = BookingEntity.builder()
                .id(99L)
                .mobil(sampleMobil)
                .tanggalMulai(LocalDate.now().plusDays(1))
                .durasiHari(5)
                .status("APPROVED")
                .build();

        when(bookingRepository.findActiveBookingsByMobilId(5L)).thenReturn(List.of(existingBooking));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> bookingService.createBooking(requestDTO));
        assertTrue(ex.getMessage().contains("sudah dipesan pada rentang tanggal"));
        verify(bookingRepository, never()).save(any(BookingEntity.class));
    }
}
