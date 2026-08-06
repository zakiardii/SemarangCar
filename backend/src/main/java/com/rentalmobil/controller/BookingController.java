package com.rentalmobil.controller;

import com.rentalmobil.dto.BookingRequestDTO;
import com.rentalmobil.entity.BookingEntity;
import com.rentalmobil.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingEntity> createBooking(@Valid @RequestBody BookingRequestDTO dto) {
        BookingEntity booking = bookingService.createBooking(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping
    public ResponseEntity<List<BookingEntity>> getAllBookings() {
        List<BookingEntity> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingEntity>> getBookingByUser(@PathVariable Long userId) {
        List<BookingEntity> bookings = bookingService.getBookingByUser(userId);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BookingEntity> updateBookingStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String newStatus = request.get("status");
        BookingEntity updated = bookingService.updateBookingStatus(id, newStatus);
        return ResponseEntity.ok(updated);
    }
}
