package com.rentalmobil.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
