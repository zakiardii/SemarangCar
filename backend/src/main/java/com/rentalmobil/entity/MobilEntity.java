package com.rentalmobil.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mobil")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobilEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nama;

    @Column(nullable = false)
    private String merk;

    @Column(nullable = false)
    private Double harga;

    @Column(nullable = false)
    private String transmisi;

    @Column(nullable = false)
    private Integer kapasitas;

    @Column(nullable = false)
    private String bahanBakar;

    @Column(nullable = false)
    private Boolean statusTersedia;

    @Column(columnDefinition = "TEXT")
    private String deskripsi;

    private String gambarUrl;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
