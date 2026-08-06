package com.rentalmobil.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "mobil_id", nullable = false)
    private MobilEntity mobil;

    @Column(nullable = false)
    private LocalDate tanggalMulai;

    @Column(nullable = false)
    private Integer durasiHari;

    @Column(nullable = false)
    private Boolean denganSopir;

    @Column(columnDefinition = "TEXT")
    private String catatan;

    @Column(nullable = false)
    private Double totalHarga;

    @Column(nullable = false)
    private String status;

    @Column(name = "metode_pembayaran")
    private String metodePembayaran;

    @Column(name = "bukti_pembayaran_url", columnDefinition = "TEXT")
    private String buktiPembayaranUrl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = StatusBooking.PENDING.name();
        }
        if (this.metodePembayaran == null) {
            this.metodePembayaran = "COD";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
