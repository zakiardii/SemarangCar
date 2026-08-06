package com.rentalmobil.service;

import com.rentalmobil.dto.BookingRequestDTO;
import com.rentalmobil.entity.BookingEntity;
import com.rentalmobil.entity.MobilEntity;
import com.rentalmobil.entity.UserEntity;
import com.rentalmobil.exception.BadRequestException;
import com.rentalmobil.exception.ResourceNotFoundException;
import com.rentalmobil.repository.BookingRepository;
import com.rentalmobil.repository.MobilRepository;
import com.rentalmobil.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final MobilRepository mobilRepository;

    @Transactional
    public BookingEntity createBooking(BookingRequestDTO dto) {
        UserEntity user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan dengan ID: " + dto.getUserId()));

        MobilEntity mobil = mobilRepository.findById(dto.getMobilId())
                .orElseThrow(() -> new ResourceNotFoundException("Mobil tidak ditemukan dengan ID: " + dto.getMobilId()));

        if (!mobil.getStatusTersedia()) {
            throw new BadRequestException("Mobil " + mobil.getNama() + " sedang tidak tersedia untuk disewa");
        }

        // Validasi Overlapping (Double Booking Prevention)
        java.time.LocalDate reqStart = dto.getTanggalMulai();
        java.time.LocalDate reqEnd = reqStart.plusDays(dto.getDurasiHari() - 1);

        List<BookingEntity> activeBookings = bookingRepository.findActiveBookingsByMobilId(mobil.getId());
        for (BookingEntity b : activeBookings) {
            java.time.LocalDate bStart = b.getTanggalMulai();
            java.time.LocalDate bEnd = bStart.plusDays(b.getDurasiHari() - 1);

            if (!reqStart.isAfter(bEnd) && !reqEnd.isBefore(bStart)) {
                throw new BadRequestException("Mobil " + mobil.getNama() + " sudah dipesan pada rentang tanggal " 
                    + bStart + " s/d " + bEnd + ". Silakan pilih tanggal lain.");
            }
        }

        double tarifSopir = Boolean.TRUE.equals(dto.getDenganSopir()) ? 150000.0 : 0.0;
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
                .metodePembayaran(dto.getMetodePembayaran() != null ? dto.getMetodePembayaran() : "COD")
                .buktiPembayaranUrl(dto.getBuktiPembayaranUrl())
                .build();

        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingEntity> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<BookingEntity> getBookingByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User tidak ditemukan dengan ID: " + userId);
        }
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public BookingEntity updateBookingStatus(Long id, String status) {
        BookingEntity booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pesanan tidak ditemukan dengan ID: " + id));

        if (status == null || status.trim().isEmpty()) {
            throw new BadRequestException("Status baru tidak boleh kosong");
        }

        booking.setStatus(status.toUpperCase());
        return bookingRepository.save(booking);
    }
}
