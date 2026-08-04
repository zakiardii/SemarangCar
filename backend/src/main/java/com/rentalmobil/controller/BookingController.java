package com.rentalmobil.controller;

import com.rentalmobil.dto.BookingRequestDTO;
import com.rentalmobil.entity.BookingEntity;
import com.rentalmobil.entity.MobilEntity;
import com.rentalmobil.entity.UserEntity;
import com.rentalmobil.repository.BookingRepository;
import com.rentalmobil.repository.MobilRepository;
import com.rentalmobil.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MobilRepository mobilRepository;

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequestDTO dto) {
        UserEntity user = userRepository.findById(dto.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User tidak ditemukan");
        }

        MobilEntity mobil = mobilRepository.findById(dto.getMobilId()).orElse(null);
        if (mobil == null || !mobil.getStatusTersedia()) {
            return ResponseEntity.badRequest().body("Mobil tidak tersedia atau tidak ditemukan");
        }

        double tarifSopir = dto.getDenganSopir() ? 150000.0 : 0.0;
        double totalHarga = (mobil.getHarga() + tarifSopir) * dto.getDurasiHari();

        BookingEntity booking = BookingEntity.builder()
                .user(user)
                .mobil(mobil)
                .tanggalMulai(dto.getTanggalMulai())
                .durasiHari(dto.getDurasiHari())
                .denganSopir(dto.getDenganSopir())
                .catatan(dto.getCatatan())
                .totalHarga(totalHarga)
                .status("PENDING")
                .build();

        BookingEntity savedBooking = bookingRepository.save(booking);
        return ResponseEntity.ok(savedBooking);
    }

    // Ambil Semua Pesanan (Untuk Admin)
    @GetMapping
    public ResponseEntity<List<BookingEntity>> getAllBookings() {
        return ResponseEntity.ok(bookingRepository.findAll());
    }

    // Ambil Pesanan milik User tertentu
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingEntity>> getBookingByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingRepository.findByUserIdOrderByCreatedAtDesc(userId));
    }

    // UPDATE STATUS BOOKING (Setujui / Tolak oleh Admin)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        BookingEntity booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            return ResponseEntity.notFound().build();
        }

        String newStatus = request.get("status");
        if (newStatus != null) {
            booking.setStatus(newStatus.toUpperCase());
            bookingRepository.save(booking);
            return ResponseEntity.ok(booking);
        }

        return ResponseEntity.badRequest().body("Status tidak valid");
    }
}
